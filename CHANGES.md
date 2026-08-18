# Driver isolation refactor

## Problem

`DriverSingleton` held one static `WebDriver` for the entire test run (created once by
`ExecutionListener` at test-plan start, quit at test-plan end). `BaseTest` exposed it as a
`static` field, so every test class — and every `@Test` method within a class — ran against
the same browser tab, sharing cookies, localStorage, and in-flight DevTools network
observers. Several classes made this worse by caching page objects/helpers once in
`@BeforeAll` and reusing them across all tests in the class, or by reaching into
`BaseTest`'s static field directly without even extending it.

## Fix

Each test method now gets its own `WebDriver`, created in `@BeforeEach` and quit in
`@AfterEach`, so tests are fully independent.

### New: `src/main/java/org/example/tools/driver/DriverProvider.java`
- Provider-pattern replacement for `DriverSingleton`. Backed by a
  `ThreadLocal<WebDriver>`, so no two threads/tests ever share a driver.
- Two-method API:
  - `get()` — returns the current thread's driver, lazily creating one (Chrome,
    version 143, maximized) on first call.
  - `remove()` — quits the current thread's driver and clears the slot.

### Deprecated: `src/main/java/org/example/tools/driver/DriverSingleton.java`
- Reduced to a `@Deprecated` shim that forwards `getDriver()`/`initDriver()`/
  `closeDriver()` to `DriverProvider`. It only remains because it couldn't be deleted
  in the session that introduced `DriverProvider`. No code references it anymore —
  safe to delete this file.

### `src/test/java/org/example/tools/tests/BaseTest.java`
- `driver`, `devTools`, `responseListener` changed from `static` to instance fields.
- Added `@BeforeEach initDriver()` — `driver = DriverProvider.get()`, then builds the
  DevTools session and `ChromeResponseListener`.
- Added `@AfterEach tearDownDriver()` — destroys the response listener and calls
  `DriverProvider.remove()`.
- JUnit 5 runs superclass `@BeforeEach` before subclass `@BeforeEach`, and subclass
  `@AfterEach` before superclass `@AfterEach`, so `driver` is always ready before subclass
  setup and always torn down last.

### `src/test/java/org/example/tools/infra/ExecutionListener.java`
- No longer calls driver init/close at test-plan start/end (that's what caused the
  one-browser-for-the-whole-run behavior). Reduced to a no-op.
- No longer needed at all. Safe to delete this file **and** its ServiceLoader
  registration at
  `src/test/resources/META-INF/services/org.junit.platform.launcher.TestExecutionListener`.
  (Both were left in place only because they couldn't be deleted in-session.)

### `ContactTest.java`, `HeaderTest.java`
- Were `@TestInstance(PER_CLASS)` with `@BeforeAll` opening a page once and reusing it
  across every `@Test` in the class. Converted to `@BeforeEach`/`@AfterEach`, dropped
  `@TestInstance(PER_CLASS)`.

### `CheckoutCartPageTest.java`, `CheckoutPaymentPageTest.java`
- Built a `CheckoutTestHelper(driver)` in a `static @BeforeAll`, which also wouldn't
  compile once `driver` became an instance field. Moved helper construction into
  `@BeforeEach`; `checkoutHelper` field is no longer `static`.

### `ProductPageTest.java`
- `homePage`/`productPage` were built via field initializers (`= new HomePage(driver)`),
  which run before `BaseTest`'s `@BeforeEach` sets `driver` — would have captured a `null`
  driver. Moved both into `@BeforeEach setUpProductPage()`.

### `ProfileDisplayTest.java`, `ProfileUpdateTest.java`
- Didn't extend `BaseTest` at all — they static-imported `BaseTest.driver` directly,
  relying on `BaseTest`'s static initializer running as a side effect of class loading.
  Now properly `extends BaseTest`.

## Manual cleanup still recommended

Two dead files couldn't be deleted from within the session that made these changes;
delete them when convenient (nothing references either):

- `src/main/java/org/example/tools/driver/DriverSingleton.java` (now a deprecated shim)
- `src/test/java/org/example/tools/infra/ExecutionListener.java` (now a no-op) and
  `src/test/resources/META-INF/services/org.junit.platform.launcher.TestExecutionListener`

## Not yet verified

Full `./gradlew compileTestJava` couldn't be run in the sandbox this refactor was done in
(project requires JDK 17, sandbox only had JDK 11, and there was no network route to
Maven Central or Gradle's distribution server). All changes were reviewed manually for
compile-correctness and JUnit 5 lifecycle ordering. Run the build locally to confirm
before merging.
