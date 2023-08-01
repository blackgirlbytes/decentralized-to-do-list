// Import the necessary modules
const { test, expect } = require('@playwright/test');
import { Eyes, Target } from '@applitools/eyes-playwright';

// Existing tests
test('it should have a title', async ({ page }) => {
  await page.goto('http://localhost:3000');
  await expect(page).toHaveTitle('Web5 To Do App');
});

test('it should load the homepage', async ({ page }) => {
  await page.goto('http://localhost:3000');
  const eyes = new Eyes();
  await eyes.open(page, 'To', 'Do');
  await eyes.check(Target.window().fully())
  await eyes.close();
});

// test('it should add a task', async ({ page }) => {
//   await page.goto('http://localhost:3000');
//   const eyes = new Eyes();
//   await eyes.open(page, 'To', 'Do');

//   // Fill in the task input and click the add button
//   await page.fill('input[placeholder="New task"]', 'Test task');
//   await page.click('button[type="submit"]');

//   // Check the page visually
//   await eyes.check('After adding a task', Target.window().fully());

//   await eyes.close();
// });


