# Dunder Mifflin Infinity

Dunder Mifflin Infinity is a Play Framework 3.0.8 web application built with Scala 2.13.16. It serves as a corporate website for Dunder Mifflin's digital transformation initiative.

**Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.**

## Prerequisites and Setup

### Required Software
- Java 17+ (OpenJDK 17+ recommended)  
- SBT (Scala Build Tool) 1.11.3+

### Installation Commands
Install SBT on Ubuntu/Debian systems:
```bash
# Download and install SBT manually
curl -fL "https://github.com/sbt/sbt/releases/download/v1.11.3/sbt-1.11.3.tgz" | tar -xz
sudo mv sbt/bin/sbt /usr/local/bin/
sudo mv sbt/bin/sbt-launch.jar /usr/local/bin/
```

Verify installation:
```bash
java -version  # Should show Java 17+
sbt --version  # Should show SBT 1.11.3+
```

## Building and Running

### Development Workflow
Always run these commands from the repository root directory:

**Compile the application:**
```bash
sbt compile
```
- Takes approximately 30 seconds. NEVER CANCEL. Set timeout to 90+ seconds.
- Downloads dependencies on first run (longer initial compile time expected)

**Run tests:**
```bash
sbt test  
```
- Takes approximately 10 seconds. NEVER CANCEL. Set timeout to 60+ seconds.
- Runs 3 ScalaTest specs that test the HomeController functionality
- All tests should pass consistently

**Run development server:**
```bash
sbt run
```
- Starts development server on port 9000 with auto-reload enabled
- Takes 30+ seconds to start. NEVER CANCEL. Set timeout to 120+ seconds.
- Press Enter to stop the server
- Access application at http://localhost:9000/

**Package for production:**
```bash
sbt stage
```
- Takes approximately 10 seconds. NEVER CANCEL. Set timeout to 60+ seconds.
- Creates staged application in `target/universal/stage/`
- Production binary: `./target/universal/stage/bin/infinity`
- **Note:** Staged application requires `play.http.secret.key` configuration for production mode

### CI/CD Integration
The application uses GitHub Actions (`.github/workflows/build.yml`):
- Runs on Java 11 in CI (compatible with local Java 17+ development)
- Executes: `sbt compile`, `sbt test`, `sbt stage`
- Uses SBT dependency caching for faster builds

## Manual Validation Scenarios

**Always test these scenarios after making changes:**

1. **Homepage Content Validation:**
   ```bash
   # Start the application (sbt run)
   curl http://localhost:9000/
   # Verify response contains:
   # - "DUNDER MIFFLIN" header with infinity symbol
   # - "Ryan Howard Announces Revolutionary Digital Initiative"  
   # - "Employee Spotlight" section
   # - HTTP 200 status code
   ```

2. **Test Suite Validation:**
   ```bash
   sbt test
   # Verify all 3 tests pass:
   # - HomeController index from new instance
   # - HomeController index from application
   # - HomeController index from router
   ```

3. **Build Artifact Validation:**
   ```bash
   sbt stage
   ls -la target/universal/stage/bin/infinity
   # Verify executable is created successfully
   ```

## Project Structure

### Key Directories
- `app/` - Application source code
  - `controllers/` - Play Framework controllers (HomeController.scala)
  - `views/` - Twirl HTML templates (main.scala.html, index.scala.html)
- `conf/` - Configuration files
  - `routes` - URL routing configuration
  - `application.conf` - Play application configuration
- `test/` - Test source code  
  - `controllers/` - Controller test specs
- `public/` - Static assets
  - `stylesheets/` - CSS files (infinity.css)
  - `javascripts/` - JavaScript files
  - `images/` - Image assets
- `project/` - SBT build configuration
  - `build.properties` - SBT version (1.11.3)
  - `plugins.sbt` - SBT plugins (Play Framework, Giter8)

### Main Application Components
- **HomeController:** Handles GET requests to "/" route, renders main page
- **Main Template:** Base HTML layout with navigation, header, footer
- **Index Template:** Homepage content with news sections and employee spotlight
- **Infinity CSS:** Custom styling matching Dunder Mifflin branding

## Common Issues and Limitations

### Production Deployment
- Staged application requires `play.http.secret.key` configuration
- Production mode differs from development mode (stricter security requirements)
- Use `sbt run` for development, not the staged binary

### Build Performance  
- Initial compile downloads all dependencies (can take 2+ minutes)
- Subsequent compiles are much faster due to incremental compilation
- SBT shell mode (`sbt` then commands) is more efficient for multiple operations

### No Code Formatting
- Project does not include Scalafmt or other code formatting tools
- Follow existing code style in the repository
- No linting tools configured

## Development Commands Summary

| Command | Purpose | Time | Timeout |
|---------|---------|------|---------|
| `sbt compile` | Compile Scala sources | ~30s | 90s+ |
| `sbt test` | Run test suite | ~10s | 60s+ |  
| `sbt run` | Start dev server | ~30s startup | 120s+ |
| `sbt stage` | Package for production | ~10s | 60s+ |
| `sbt clean` | Clean build artifacts | ~5s | 30s+ |

**CRITICAL:** Never cancel long-running SBT commands. They are downloading dependencies or performing incremental compilation. Wait for completion.