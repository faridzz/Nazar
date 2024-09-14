# Nazar

Nazar is a Spring-based application designed to automatically collect and store user reviews for specific products from various websites. Ideal for businesses and researchers needing real-time customer sentiment analysis and product feedback monitoring.

## Features
- **Automated Review Collection**: Gathers product reviews from multiple sources.
- **Efficient Data Storage**: Stores reviews in a database for easy access and analysis.
- **Customizable Product Types**: Add various product types (e.g., "Books", "Laptops") or send product details directly in the search request.
- **Scalable and Robust**: Built using Spring Framework.

## Getting Started

### Prerequisites
- **Java** 17 or later
- **Gradle** 7 or later
- **Spring Boot** 3.x

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/faridzz/Nazar.git
    ```
2. Navigate to the project directory:
    ```bash
    cd Nazar
    ```
3. Build the project:
    ```bash
    ./gradlew build
    ```

### Configuration
Ensure database settings are properly configured in the `application.yml` file.

## How to Use

### Search for Products and Collect Reviews
Users can search for products across multiple websites and gather reviews by sending a JSON request directly to the `/api/search` endpoint. The `typeName` and `sitesUrl` are optional fields. Here’s how to do it:

```bash
curl -X POST -H "Content-Type: application/json" -d '{
    "sitesUrl":["www.mobile.ir","www.digikala.com"],
    "typeName":"smartphone",
    "productName":"xiaomi poco x3 pro"
}' http://localhost:8081/api/search
````
### Response Example
The response will contain data from the provided sites, listing product information:
```json
[
   {
      "siteType": "MOBILE",
      "typeName": "smartphone",
      "baseDTO": [
         {
            "title": "xiaomi poco x3 pro",
            "id": 39237,
            "image": "//s.mobile.ir/static/cache/phonethumbs/39237_-1.jpg",
            "url": "/phones/specifications-39237-xiaomi-poco-x3-pro.aspx"
         },
         {
            "title": "xiaomi poco x3",
            "id": 39077,
            "image": "//s.mobile.ir/static/cache/phonethumbs/39077_-1.jpg",
            "url": "/phones/specifications-39077-xiaomi-poco-x3.aspx"
         }
      ]
   },
   {
      "siteType": "DIGIKALA",
      "typeName": "smartphone",
      "baseDTO": [
         {
            "title": "xiaomi poco x6 pro 5g dual sim 512gb and 12gb ram mobile phone",
            "id": 14214794,
            "image": "https://dkstatics-public.digikala.com/digikala-products/fef4d75b01a352241add7ae5cf2fc04130693dc1_1706096962.jpg",
            "url": "/product/dkp-14214794/گوشی-موبایل-شیائومی-مدل-poco-x6-pro-دو-سیم-کارت-ظرفیت-512-گیگابایت-و-رم-12-گیگابایت"
         }
      ]
   }
]
```
### Add Reviews (Accurate Method)
To add reviews for a specific product accurately, send a POST request to /api/add/one/accurate with a list of detailed product information:
```bash
curl -X POST -H "Content-Type: application/json" -d '[
  {
    "siteType": "DIGIKALA",
    "typeName": "smartphone",
    "baseDTO": {
      "title": "xiaomi poco x6 pro 5g dual sim 512gb and 12gb ram mobile phone",
      "id": 14214794,
      "image": "https://dkstatics-public.digikala.com/digikala-products/fef4d75b01a352241add7ae5cf2fc04130693dc1_1706096962.jpg",
      "url": "/product/dkp-14214794/گوشی-موبایل-شیائومی-مدل-poco-x6-pro-دو-سیم-کارت-ظرفیت-512-گیگابایت-و-رم-12-گیگابایت"
    }
  }
]' http://localhost:8081/api/add/one/accurate
```
### Response Example:
A success message will be returned with the added review information.
```bash
curl -X POST -H "Content-Type: application/json" -d '{"name": "Example Site", "url": "https://example.com"}' http://localhost:8080/api/sites
```
### Add a New Site
If needed, new review websites can be added using the following endpoint:

```bash
Copy code
curl -X POST -H "Content-Type: application/json" -d '{
"name": "Example Site",
"url": "https://example.com"
}' http://localhost:8081/api/sites
````
 