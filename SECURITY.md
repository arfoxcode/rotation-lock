# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.x.x   | ✅ Yes             |

Only the latest stable release receives security updates.

## Reporting a Vulnerability

**Do not report security vulnerabilities via GitHub Issues.** Issues are public and visible to everyone.

Instead, please use one of the following:

1. **GitHub Security Advisories** (preferred):
   Go to [Security > Advisories > New draft advisory](https://github.com/arfoxcode/rotation-lock/security/advisories/new)

2. **GitHub Private Vulnerability Reporting**:
   Use the "Report a vulnerability" button on the Security tab.

### What to Include

Please provide:

- A description of the vulnerability and its potential impact
- Steps to reproduce the issue
- Affected version(s)
- Any relevant logs or screenshots (redact sensitive data)
- Your suggested fix, if any

### What to Expect

- Acknowledgement within **72 hours**
- Status update within **7 days**
- Fix or mitigation plan within **30 days** for confirmed vulnerabilities

### Scope

This app:
- Does **not** collect user data
- Does **not** make network requests
- Does **not** store sensitive information beyond local preferences
- Requires only `WRITE_SETTINGS` permission

Security concerns most likely to be relevant:
- Improper permission usage
- Data leakage via logs or exported components
- Unintended behavior when permission is granted by malicious actors
