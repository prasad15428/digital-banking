# Account Management Feature - API Documentation

## Overview
The Account Management feature provides comprehensive banking account functionality including account creation, balance tracking, deposit/withdrawal operations, and complete transaction history.

## Entities

### Account Entity
- **ID**: Unique identifier (auto-generated)
- **User**: Reference to the User who owns the account
- **Account Number**: Unique 20-character account number (auto-generated format: SBI + timestamp + UUID)
- **Account Type**: Savings, Current, Business, or Salary
- **Balance**: Current account balance (BigDecimal with 2 decimal places)
- **Status**: Active, Inactive, or Suspended
- **Created At**: Account creation timestamp
- **Updated At**: Last update timestamp

### Transaction Entity
- **ID**: Unique identifier (auto-generated)
- **Account**: Reference to the Account
- **Transaction Type**: Deposit, Withdrawal, Transfer, or Purchase
- **Amount**: Transaction amount
- **Description**: Optional transaction description
- **Balance After**: Account balance after the transaction
- **Transaction Date**: When the transaction occurred
- **Status**: Success, Failed, or Pending

## API Endpoints

### Account Management

#### 1. Create Account
```
POST /api/accounts/create/{userId}
```
**Description**: Create a new bank account for a user

**Request Parameters**:
- `userId` (path): User ID

**Request Body**:
```json
{
  "accountType": "Savings",
  "initialBalance": 50000.00
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "accountNumber": "SBI1706686584500A1B2C3D4",
  "accountType": "Savings",
  "balance": 50000.00,
  "status": "Active",
  "createdAt": "2026-01-31T09:30:00",
  "updatedAt": "2026-01-31T09:30:00",
  "message": "Account created successfully",
  "success": true
}
```

#### 2. Get Account Details
```
GET /api/accounts/{accountId}
```
**Description**: Retrieve detailed information about a specific account

**Response** (200 OK):
```json
{
  "id": 1,
  "accountNumber": "SBI1706686584500A1B2C3D4",
  "accountType": "Savings",
  "balance": 50000.00,
  "status": "Active",
  "createdAt": "2026-01-31T09:30:00",
  "updatedAt": "2026-01-31T09:30:00",
  "message": "Account details retrieved",
  "success": true
}
```

#### 3. Get All User Accounts
```
GET /api/accounts/user/{userId}
```
**Description**: Retrieve all accounts owned by a specific user

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "accountNumber": "SBI1706686584500A1B2C3D4",
    "accountType": "Savings",
    "balance": 50000.00,
    "status": "Active",
    "createdAt": "2026-01-31T09:30:00",
    "updatedAt": "2026-01-31T09:30:00",
    "message": "Account retrieved",
    "success": true
  },
  {
    "id": 2,
    "accountNumber": "SBI1706686584600B2C3D4E5",
    "accountType": "Current",
    "balance": 100000.00,
    "status": "Active",
    "createdAt": "2026-01-31T09:35:00",
    "updatedAt": "2026-01-31T09:35:00",
    "message": "Account retrieved",
    "success": true
  }
]
```

#### 4. Get Account Balance
```
GET /api/accounts/{accountId}/balance
```
**Description**: Get the current balance of an account

**Response** (200 OK):
```json
50000.00
```

### Transaction Operations

#### 5. Deposit Money
```
POST /api/accounts/{accountId}/deposit
```
**Description**: Deposit money into an account

**Request Body**:
```json
{
  "transactionType": "Deposit",
  "amount": 10000.00,
  "description": "Salary credit"
}
```

**Response** (200 OK):
```json
{
  "transactionId": 1,
  "transactionType": "Deposit",
  "amount": 10000.00,
  "description": "Salary credit",
  "balanceAfter": 60000.00,
  "transactionDate": "2026-01-31T09:45:00",
  "status": "Success"
}
```

#### 6. Withdraw Money
```
POST /api/accounts/{accountId}/withdraw
```
**Description**: Withdraw money from an account

**Request Body**:
```json
{
  "transactionType": "Withdrawal",
  "amount": 5000.00,
  "description": "ATM withdrawal"
}
```

**Response** (200 OK):
```json
{
  "transactionId": 2,
  "transactionType": "Withdrawal",
  "amount": 5000.00,
  "description": "ATM withdrawal",
  "balanceAfter": 55000.00,
  "transactionDate": "2026-01-31T09:50:00",
  "status": "Success"
}
```

#### 7. Transfer Money
```
POST /api/accounts/{fromAccountId}/transfer/{toAccountId}
```
**Description**: Transfer money from one account to another

**Request Body**:
```json
{
  "transactionType": "Transfer",
  "amount": 15000.00,
  "description": "Payment to friend"
}
```

**Response** (200 OK):
```json
{
  "transactionId": 3,
  "transactionType": "Transfer",
  "amount": 15000.00,
  "description": "Transfer from SBI1706686584500A1B2C3D4: Payment to friend",
  "balanceAfter": 85000.00,
  "transactionDate": "2026-01-31T10:00:00",
  "status": "Success"
}
```

### Transaction History

#### 8. Get Transaction History
```
GET /api/accounts/{accountId}/transactions
```
**Description**: Retrieve all transactions for an account

**Optional Query Parameters**:
- `limit` (integer): Limit the number of transactions returned

**Example**: `/api/accounts/1/transactions?limit=10`

**Response** (200 OK):
```json
[
  {
    "transactionId": 3,
    "transactionType": "Transfer",
    "amount": 15000.00,
    "description": "Transfer from SBI1706686584500A1B2C3D4: Payment to friend",
    "balanceAfter": 85000.00,
    "transactionDate": "2026-01-31T10:00:00",
    "status": "Success"
  },
  {
    "transactionId": 2,
    "transactionType": "Withdrawal",
    "amount": 5000.00,
    "description": "ATM withdrawal",
    "balanceAfter": 55000.00,
    "transactionDate": "2026-01-31T09:50:00",
    "status": "Success"
  },
  {
    "transactionId": 1,
    "transactionType": "Deposit",
    "amount": 10000.00,
    "description": "Salary credit",
    "balanceAfter": 60000.00,
    "transactionDate": "2026-01-31T09:45:00",
    "status": "Success"
  }
]
```

#### 9. Health Check
```
GET /api/accounts/health
```
**Description**: Check if Account Service is running

**Response** (200 OK):
```
Account Service is running
```

## Supported Account Types
- **Savings**: For individuals to save money
- **Current**: For business transactions
- **Business**: For business entities
- **Salary**: For salary accounts linked to employers

## Transaction Types
- **Deposit**: Money added to the account
- **Withdrawal**: Money removed from the account
- **Transfer**: Money moved to another account
- **Purchase**: Money spent on purchases

## Error Handling

### Common Error Responses

#### 404 Not Found
```json
{
  "error": "Account not found with id: 99"
}
```

#### 400 Bad Request
```json
{
  "error": "Insufficient balance for withdrawal"
}
```

#### 400 Bad Request (Validation Error)
```json
{
  "error": "Invalid account type. Allowed types: Savings, Current, Business, Salary"
}
```

## Key Features

1. **Automatic Account Number Generation**: Unique account numbers are auto-generated
2. **Balance Tracking**: Real-time balance updates with every transaction
3. **Transaction History**: Complete audit trail of all transactions
4. **Multiple Account Types**: Support for different account categories
5. **Transaction Validation**: Prevents overdrafts and invalid operations
6. **Transactional Safety**: All operations are atomic and consistent
7. **Account Status Management**: Active, Inactive, or Suspended status

## Validation Rules

1. **Account Type**: Must be one of the supported types (Savings, Current, Business, Salary)
2. **Initial Balance**: Must be positive (≥ 0)
3. **Transaction Amount**: Must be positive and not exceed account balance
4. **Account Status**: Account must be "Active" for transactions
5. **Balance Precision**: All balances are stored with 2 decimal places

## Business Logic

### Account Creation
- User must exist
- Account type must be valid
- Initial balance must be non-negative
- First transaction is logged if initial balance > 0

### Deposits
- Account must be active
- Amount must be positive
- Balance is updated immediately
- Transaction is recorded

### Withdrawals
- Account must be active
- Amount must be positive
- Sufficient balance must exist
- Transaction is recorded

### Transfers
- Both accounts must exist and be active
- Amount must be positive
- Source account must have sufficient balance
- Transactions are recorded for both accounts

## Database Schema

### Tables Created
1. **users** - User information
2. **accounts** - Bank accounts
3. **transactions** - Transaction history

### Foreign Keys
- `accounts.user_id` → `users.id`
- `transactions.account_id` → `accounts.id`

## Service Classes

### AccountService
Handles all business logic for account management:
- `createAccount()` - Create new account
- `getAccountById()` - Retrieve account
- `getAccountDetails()` - Get account details as DTO
- `getUserAccounts()` - Get all accounts for a user
- `deposit()` - Perform deposit
- `withdraw()` - Perform withdrawal
- `transfer()` - Transfer between accounts
- `getTransactionHistory()` - Retrieve transactions

### AccountController
REST endpoints for account operations:
- All endpoints are CORS-enabled
- All endpoints validate input using Jakarta validation
- Appropriate HTTP status codes are returned

## Integration

The Account Management feature integrates seamlessly with:
- User management (User entity reference)
- Spring Data JPA for persistence
- H2 database for development/testing
- Spring Boot validation framework

## Future Enhancements

Potential improvements:
- Interest calculation for savings accounts
- Overdraft protection
- Recurring transactions
- Bill payments
- Loan management
- Account statements generation
- PDF export of transactions
- Monthly statements
