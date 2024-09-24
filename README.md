# Bati-Cuisine

## Description

Bati-Cuisine is a Java-based application designed specifically for construction professionals to efficiently manage renovation and construction projects. This application enables users to keep track of project details, associated costs, and client information, all while ensuring a user-friendly experience through an intuitive console interface.

The application leverages modern Java features and design patterns to provide a robust solution for estimating project costs, managing labor and materials, and generating transparent quotes for clients.

## Features

- **Project Management**: Create and manage renovation or construction projects, allowing users to track all relevant details and costs.
- **Client Association**: Link each project to a specific client, facilitating easy access to client information for billing and quoting purposes.
- **Material Tracking**: Add materials to projects with their unit cost, quantity, and transportation details for precise cost estimation.
- **Labor Management**: Record worker hours, including hourly rates and productivity, to calculate total labor costs accurately.
- **Cost Calculation**: Manage various types of materials and specialized workers to calculate costs based on quality and expertise requirements.
- **Quote Generation**: Generate detailed quotes based on estimated costs of materials, labor, and equipment, providing clear and transparent estimates to clients.
- **Date Management**: Specify issue and validity dates for quotes, ensuring clients are informed about the timeframe of offers.
- **Client Acceptance**: Enable clients to accept or reject quotes, facilitating smooth project progression based on client agreements.
- **Client Information Management**: Store and manage client information for easy follow-ups and billing.
- **Client Differentiation**: Distinguish between professional and individual clients to apply specific discounts or conditions accordingly.
- **Comprehensive Cost Overview**: Calculate the total project cost, incorporating materials, labor, equipment, and taxes for a complete financial overview.
- **Adjustable Cost Parameters**: Allow managers to adjust costs based on material quality or worker productivity, ensuring accurate final estimates.
- **Tax Visibility**: Display taxes applied to each project component, including VAT and other charges, in the final estimates.

## Technical Requirements

- **Java 8**
- **PostgreSQL Database**
- **PostgreSQL JDBC Driver** (included in `lib/postgresql-42.7.4.jar`)
- **Design Patterns**: Singleton, Repository Pattern
- **Version Control**: Git and JIRA

## How to Run the Application

1. **Create the PostgreSQL Database**:

   ```sql
   CREATE DATABASE Bati;
   ```

   - Import the Bati.sql file to set up the necessary tables.

2. **Configure Database Credentials**:
   The application uses environment variables to manage database credentials (DB_USERNAME and DB_PASSWORD). Before running the application, ensure that the following environment variables are set in your system:

   ```
    For Linux/macOS: Add the following to your .bashrc, .bash_profile, or .zshrc file, and then run source to load the new configuration:
        export DB_USERNAME='your_postgres_username'
        export DB_PASSWORD='your_postgres_password'
    For Windows: Set the environment variables using the command prompt:
        setx DB_USERNAME "your_postgres_username"
        setx DB_PASSWORD "your_postgres_password"
   ```

3. **Run the JAR File**: Navigate to the directory where the JAR file is located, and execute the following command:

```bash
    java -jar Bati-Cuisine.jar
```

**JIRA Project**
For task management and progress tracking, please refer to the JIRA project: https://naoufallabrihmi.atlassian.net/jira/software/projects/BATI/boards/1

## UML Class Diagram

## License

This project is licensed under the MIT License. See the [LICENSE.md](LICENSE.md) file for details.
