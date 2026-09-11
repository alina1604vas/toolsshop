# ToolShop UI Test Automation

Automated end-to-end UI test suite for [Practice Software Testing (Toolshop)](https://practicesoftwaretesting.com/) — an e-commerce demo application. Built with **Java 17**, **Selenium WebDriver**, **JUnit 5**, and the **Page Object Model** pattern, with **Allure** for test reporting.

## Tech stack

- **Java 17**
- **Gradle 8.8** (via wrapper — no local Gradle install required)
- **Selenium WebDriver 4.47** — browser automation (Chrome)
- **JUnit 5 (Jupiter)** — test framework, including parameterized tests and JUnit Platform Suites
- **Allure 2.24** — test reporting
- **DataFaker** — random test data generation
- **Awaitility** — async/wait condition handling
- **Gson** — JSON handling

## Project structure

```
src/
├── main/java/org/example/tools/
│   ├── SystemConfig.java          # Base URL configuration (env var / system property / default)
│   ├── driver/
│   │   └── DriverProvider.java    # Thread-safe WebDriver factory (one driver per test)
│   ├── network/                   # Chrome DevTools network listeners/diagnostics
│   ├── pageobject/                # Page Object classes (one per page/component)
│   │   └── entity/                # Supporting data objects (cart, address, etc.)
│   └── utils/                     # Credentials, test data, user factories, helpers
│
└── test/java/org/example/tools/
    ├── suites/                    # JUnit Platform test suite definitions
    ├── tests/                     # Test classes (one per page/feature area)
    │   └── utils/                 # Test-only helpers
    ├── extensions/                # JUnit extensions (screenshots on failure, Allure attachments)
    └── infra/                     # Test execution listeners

src/test/resources/                # CSV files driving parameterized tests, service loader config
credentials.properties             # Local test-user credentials (not committed with real values)
build.gradle                       # Build & dependency configuration
.github/workflows/ci.yml           # CI pipeline (GitHub Actions)
```

### Design patterns & principles used

- **Page Object Model (POM)** — every page/component under test (`HomePage`, `ProductPage`, `CheckoutCartPage`, etc., in `pageobject/`) encapsulates its own locators (`@FindBy`) and interactions behind methods, so selectors never leak into test classes. Selenium's `PageFactory.initElements()` wires up the `@FindBy` fields on construction.
- **Fluent interface** — page object methods that navigate or set up state (e.g. `HomePage.open()`) return `this`, allowing calls to be chained (`new HomePage(driver).open()`) rather than requiring a separate statement per step.
- **Flow objects** — multi-step user journeys that cross several pages (e.g. `pageobject/flow/SignOut`) are modeled as their own small classes rather than being duplicated inline in every test that needs to sign out.
- **Test helper / facade** — `CheckoutTestHelper` wraps the several page objects and steps needed to get a product into the cart and through checkout behind a small number of high-level methods (`addRandomProductToCart(...)`), so checkout tests read as business steps instead of low-level UI actions.
- **Provider pattern (thread-confined singleton)** — `DriverProvider` hands out one `WebDriver` per thread via a `ThreadLocal`, lazily created on first use (`get()`) and torn down with `remove()`. This replaces an older true singleton (`DriverSingleton`, now a deprecated shim) that shared one browser across the whole run.
- **Builder pattern** — `Customer` is constructed through a nested `Customer.Builder`, letting optional fields (birth date, phone, password) be set fluently while required fields (name, email, billing address) stay mandatory constructor arguments.
- **Factory pattern** — `UserFactory` centralizes creation of test users (`createGuest()`, `createCustomer(country)`), pulling random-but-valid field values from `TestData` so tests never hand-assemble user objects themselves.
- **Interface segregation via a common contract** — `Guest` and `Customer` both implement the `User` interface, so checkout/registration flows that only need name/email/address can accept either without caring which concrete type is in play.
- **Value objects** — `BillingAddress`, `UiProduct`, `UiCartElement`, and `Cart` are small immutable-leaning data carriers (with `equals()`/`hashCode()`/`toString()` overridden where they're compared in assertions) that move structured data between page objects and tests instead of passing loose strings around.
- **Test data builder / object mother** — `TestData` is a static "object mother" that hands out realistic random values (names, addresses, passwords, etc.) via DataFaker, used by `UserFactory` and directly in tests, so test data generation logic lives in one place.
- **Observer pattern** — `ChromeResponseListener` registers listeners on Chrome DevTools Protocol network events (`Network.responseReceived()`), notifying registered observers when matching network responses arrive, decoupling "watch for this API response" logic from the tests that need it.
- **JUnit 5 extension model (`TestWatcher`)** — `ScreenshotOnFailureExtension` implements JUnit's `TestWatcher` to automatically capture and attach a screenshot (via `AllureAttachments`) whenever a test fails, without any explicit try/catch in the test code.
- **Template method (via JUnit lifecycle)** — `BaseTest` centralizes the common `@BeforeEach`/`@AfterEach` driver setup/teardown that all test classes inherit, so subclasses only add the steps specific to what they're testing.
- **Data-driven / parameterized tests** — several test classes use JUnit 5 `@ParameterizedTest` with CSV files from `src/test/resources/` (e.g. invalid login/registration/contact-form inputs) to run the same test logic against many input combinations.
- **Tagged tests** — tests relevant to a fast smoke run are annotated `@Tag("smoke")`, which CI runs on every push; other tags can be introduced the same way to slice the suite differently (e.g. regression, UI-only).
- **Suite composition** — `TestSuite1` uses the JUnit Platform Suite API (`@Suite`, `@SelectPackages`) to group and run whole packages of tests together.
- **Config via environment with fallback** — `SystemConfig.getBaseUrl()` and `Credentials` both resolve values by checking a system property/environment variable first and falling back to a properties file or hardcoded default, so the same code runs unmodified locally and in CI.

## Prerequisites

- **JDK 17**
- **Google Chrome** installed locally (matching ChromeDriver is resolved automatically by Selenium Manager)
- No need to install Gradle — use the bundled `./gradlew` (or `gradlew.bat` on Windows)

## Setup

1. Clone the repository.
2. Provide test-user credentials, either by:
   - Editing `credentials.properties` in the project root with real values for:
     ```
     test.user.firstName=
     test.user.lastName=
     test.user.email=
     test.user.password=
     test.user.phone=
     test.user.street=
     test.user.postalCode=
     test.user.city=
     test.user.state=
     test.user.country=
     ```
   - or setting environment variables instead (these take precedence over the properties file): `TEST_USER_EMAIL`, `TEST_USER_PASSWORD`, `TEST_USER_FIRSTNAME`, `TEST_USER_LASTNAME`, `TEST_USER_STREET`, `TEST_USER_CITY`, `TEST_USER_COUNTRY`.

   **Never commit real credentials** — keep `credentials.properties` out of version control if it contains real values (add it to `.gitignore` if it isn't already).

3. By default, tests run against the public demo site `https://practicesoftwaretesting.com/`. To point at a different environment (e.g. a local/dockerized instance), set the `BASE_URL` environment variable or pass `-DbaseUrl=...`:
   ```bash
   BASE_URL=http://localhost:4200/ ./gradlew test
   ```

## Running tests

Run the full suite:

```bash
./gradlew test
```

Run only the smoke suite:

```bash
./gradlew test -PincludeTags=smoke
```

Run headless (no visible browser window):

```bash
./gradlew test -Dheadless=true
```

> Tests automatically run headless when the `CI` environment variable is set to `true`.

## Test reports

### Allure

After running tests, generate and open an Allure report:

```bash
./gradlew allureReport
./gradlew allureServe
```

Raw results are written to `build/allure-results/`; the generated HTML report goes to `build/reports/allure-report/`.

### JUnit HTML report

Gradle's standard HTML test report is available at `build/reports/tests/test/index.html` after any run.

## Continuous Integration

GitHub Actions (`.github/workflows/ci.yml`) runs on every push to any branch:

1. Checks out this repo and the [`practice-software-testing`](https://github.com/testsmith-io/practice-software-testing) application source.
2. Spins up the application under test via Docker Compose (self-hosted, since the public demo site blocks CI runner IPs via Cloudflare) and seeds its database.
3. Runs the `smoke`-tagged tests against the locally running app, using credentials from GitHub Secrets.
4. Publishes the Allure report to the `gh-pages` branch (with history retained across runs) and uploads raw Allure results / JUnit HTML reports as workflow artifacts, even on failure.

Required GitHub Secrets for CI: `TEST_USER_EMAIL`, `TEST_USER_PASSWORD`, `TEST_USER_FIRSTNAME`, `TEST_USER_LASTNAME`, `TEST_USER_STREET`, `TEST_USER_CITY`, `TEST_USER_COUNTRY`.
