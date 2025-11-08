# Technical Difficulties and Solutions

This document summarizes the main technical challenges encountered during the development and testing process of the **BookingMx Challenge 7 frontend**, as well as the strategies and reasoning used to overcome them.

---

## 1. Running Jest with ES Modules (ESM)

### **Problem**
Initially, Jest threw the following error when running tests:
```
SyntaxError: Cannot use import statement outside a module
```
This occurred because the project uses modern **ES Modules** (`import/export` syntax) and Jest, by default, expects **CommonJS**.

### **Solution**
After testing several configurations (including Babel and `babel-jest`), the final and most efficient setup used only **Node’s native ESM support**.  
The `package.json` and Jest setup were simplified to:

```json
"scripts": {
  "serve": "npx http-server -c-1 -p 5173 .",
  "test": "node --experimental-vm-modules node_modules/jest/bin/jest.js --coverage"
}
```

And the Jest configuration file (`jest.config.js`) was:

```js
import { transform } from "typescript";

export default {
  testEnvironment: "node",
  collectCoverage: true,
  collectCoverageFrom: ["js/**/*.js"],
  coverageReporters: ["text", "lcov"],
  transform: {}
};
```

✅ This configuration worked perfectly with **pure JavaScript (ESM)** without needing Babel or transpilers.  
It also runs faster and keeps the project simpler.

---

## 2. Mocking `fetch()` in Unit Tests

### **Problem**
When testing functions that called external APIs (e.g., `listReservations()`), tests failed because `fetch` was not defined in Node’s environment.

### **Solution**
Used **Jest mocks** to simulate API responses:
```js
global.fetch = jest.fn();
fetch.mockResolvedValueOnce({
  ok: true,
  json: async () => mockData
});
```
This made it possible to test network logic without making real HTTP requests, increasing test reliability and coverage.

---

## 3. Achieving High Test Coverage (90%+)

### **Problem**
Initial test coverage was around 25%, mostly covering only one successful path.

### **Solution**
Expanded tests to include:
- Error handling (`!res.ok`)
- Edge cases (invalid input, missing data)
- Multiple API endpoints (`createReservation`, `updateReservation`, `cancelReservation`)
- Pure logic functions (`Graph`, `validateGraphData`, etc.)

By systematically covering **every branch and error path**, coverage increased to over **90%**.

---

## 4. Testing Pure Logic and Data Structures

### **Problem**
The `Graph` class included logic that could throw several errors for invalid input. Testing only normal behavior was insufficient.

### **Solution**
Wrote **unit tests for every branch**, including:
- Invalid city names
- Missing connections
- Negative or non-numeric distances
- Nonexistent nodes

This ensured the code was resilient and predictable even under incorrect usage.

---

## 5. Reflection on Technical Learning

Through this process, the following key lessons were learned:
- **Jest + ESM integration** can be done natively with Node, without Babel.
- **Mocking global APIs** like `fetch` is crucial for realistic and isolated frontend testing.
- Writing **comprehensive tests** not only improves coverage but also deepens understanding of the code’s internal logic.
- Keeping functions **pure and modular** makes them far easier to test and maintain.
- Minimal configurations often lead to **better maintainability** and **faster CI runs**.

---

## Summary

| Challenge | Solution Summary | Tools/Concepts Used |
|------------|------------------|---------------------|
| Jest not recognizing ES modules | Used native ESM with `--experimental-vm-modules` | Node, Jest |
| `fetch` not defined in tests | Mocked global fetch | Jest mocking |
| Low test coverage | Added edge-case & error tests | Branch coverage, unit testing |
| Graph class complexity | Tested invalid inputs & logic | Jest assertions |
| Maintainable test setup | Minimal, fast Jest config | Native ESM, simplicity |

---

**Author:** Angel Lomeli  
**Date:** 2025-11-08  
**Project:** BookingMx Challenge 7 – Frontend
