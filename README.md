Nazar

Nazar is a Spring-based application designed to automatically collect and store user reviews for specific products from
various websites. Ideal for businesses and researchers needing real-time customer sentiment analysis and product
feedback monitoring.

Features

Automated Review Collection: Gathers product reviews from multiple sources.
Efficient Data Storage: Stores reviews in a database for easy access and analysis.
Customizable Product Types: Add various product types (e.g., "Books", "Laptops").
Scalable and Robust: Built using Spring Framework.

Getting Started

Prerequisites

Java 17 or later

Gradle 7 or later

Spring Boot 3.x

Installation

bash

git clone https://github.com/faridzz/Nazar.git
cd Nazar
./gradlew build
Configuration
Ensure database settings are properly configured in application.properties or application.yml.

How to Use

1. Add a New Site
   bash

   curl -X POST -H "Content-Type: application/json" -d '{"name": "Example Site", "
   url": "https://example.com"}' http://localhost:8080/api/sites
2. Add a Product Type
   bash

   curl -X POST -H "Content-Type: application/json" -d '{"name": "Smartphone"}' http://localhost:8080/api/types
3. Collect Reviews for a Product
   Ensure the site and type are added first:

bash
 
curl -X POST -H "Content-Type: application/json" -d '{"typeName":"smartphone","siteUrl":"www.mobile.ir","productName":"
samsung galaxy a71"}' http://localhost:8080/api/add/one
