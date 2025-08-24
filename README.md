# Strategy Pattern Demo - E-Commerce Application

A comprehensive JavaFX application demonstrating the **Strategy Design Pattern** with modern UI, user authentication, and real-world e-commerce functionality.

## 🚀 Quick Start (For End Users)

### Prerequisites
- **Java 17 or later** installed on your system
- Download the latest release JAR file

### Running the Application

#### Windows:
```bash
# Double-click run.bat or run in command prompt:
run.bat
```

#### macOS/Linux:
```bash
# Make script executable and run:
chmod +x run.sh
./run.sh
```

#### Manual Java Command:
```bash
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -jar Strategy-Demo-1.0-SNAPSHOT-fat.jar
```

### Demo Accounts
The application comes with pre-configured demo accounts:
- **Admin:** `admin` / `admin123`
- **Customer:** `john_doe` / `password123`
- **Manager:** `manager` / `manager123`

## 🛠️ Development & Build Instructions

### Prerequisites
- **Java 21** (JDK)
- **Maven 3.6+**
- **Git** (optional)

### Building from Source

1. **Clone the repository:**
```bash
git clone <your-repo-url>
cd Strategy-Demo
```

2. **Compile the project:**
```bash
mvn clean compile
```

3. **Run during development:**
```bash
# Using JavaFX plugin:
mvn javafx:run

# Using exec plugin:
mvn exec:java
```

4. **Package into executable JAR:**
```bash
mvn clean package
```

This creates two JAR files in the `target/` directory:
- `Strategy-Demo-1.0-SNAPSHOT.jar` - Regular JAR (requires classpath)
- `Strategy-Demo-1.0-SNAPSHOT-fat.jar` - Fat JAR with all dependencies

### Project Structure
```
Strategy-Demo/
├── src/main/java/org/strategy/
│   ├── Main.java                    # Application entry point
│   ├── LoginRegisterApp.java        # Authentication UI
│   ├── ECommerceDiscountApp.java    # Main application UI
│   ├── User.java                    # User model
│   ├── UserService.java             # Authentication service
│   ├── DiscountStrategy.java        # Strategy interface
│   ├── PercentageDiscount.java      # Concrete strategy
│   ├── FixedAmountDiscount.java     # Concrete strategy
│   ├── BuyOneGetOneDiscount.java    # Concrete strategy
│   ├── NoDiscount.java              # Concrete strategy
│   ├── Product.java                 # Product model
│   └── ShoppingCart.java            # Shopping cart context
├── src/main/resources/
│   └── styles.css                   # Application styling
├── target/                          # Build output
├── run.bat                          # Windows run script
├── run.sh                           # Unix/Linux run script
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## 🎯 Strategy Pattern Implementation

### Core Components

#### 1. Strategy Interface
```java
public interface DiscountStrategy {
    double applyDiscount(double totalAmount, List<Product> products);
    String getDescription();
}
```

#### 2. Concrete Strategies
- **NoDiscount** - No discount applied
- **PercentageDiscount** - Percentage off with minimum amount
- **FixedAmountDiscount** - Fixed dollar amount off
- **BuyOneGetOneDiscount** - BOGO deals with category filtering

#### 3. Context Class
- **ShoppingCart** - Uses strategies to calculate discounts

### Key Benefits Demonstrated
- ✅ **Runtime algorithm switching**
- ✅ **Easy addition of new discount types**
- ✅ **Clean separation of concerns**
- ✅ **Testable individual strategies**
- ✅ **No conditional complexity**

## 🎨 Features

### Modern UI/UX
- **Responsive design with scroll support**
- **Professional CSS styling**
- **Smooth animations and transitions**
- **Modern card-based layout**
- **User-friendly forms with validation**

### Authentication System
- **User registration and login**
- **Password hashing with salt**
- **Session management**
- **Role-based access (Admin, Customer, Manager)**
- **User profile management**

### E-Commerce Functionality
- **Product catalog with categories**
- **Shopping cart management**
- **Real-time discount calculation**
- **Multiple discount strategies**
- **Order total calculation**

## 🚢 Distribution Options

### Option 1: Fat JAR (Recommended)
```bash
mvn clean package
# Distribute: target/Strategy-Demo-1.0-SNAPSHOT-fat.jar + run scripts
```

### Option 2: JavaFX Runtime Image
```bash
# Create a self-contained application (advanced):
mvn javafx:jlink
```

### Option 3: Native Installer
```bash
# Using jpackage (Java 14+):
jpackage --input target/ --name "Strategy-Demo" --main-jar Strategy-Demo-1.0-SNAPSHOT-fat.jar --main-class org.strategy.Main --type exe
```

## 📋 System Requirements

### Minimum Requirements
- **OS:** Windows 10, macOS 10.14, or Linux (Ubuntu 18.04+)
- **Java:** Java 17 or later
- **RAM:** 512 MB
- **Storage:** 100 MB free space

### Recommended Requirements
- **Java:** Java 21 (LTS)
- **RAM:** 1 GB
- **Storage:** 500 MB free space

## 🐛 Troubleshooting

### Common Issues

#### "Java not found" Error
- Install Java 17+ from [OpenJDK](https://openjdk.org/) or [Oracle](https://www.oracle.com/java/)
- Add Java to your system PATH

#### "Module javafx.controls not found"
- Use the provided run scripts which include JavaFX modules
- Or download JavaFX separately and add to module path

#### Application won't start
1. Ensure Java 17+ is installed: `java -version`
2. Verify JAR file exists in target/ directory
3. Run: `mvn clean package` to rebuild
4. Check console output for error messages

### Performance Optimization
- Use Java 21 for best performance
- Allocate more memory for large datasets: `java -Xmx1g -jar ...`
- Enable hardware acceleration: `java -Dprism.order=sw -jar ...`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

### Adding New Discount Strategies
1. Implement the `DiscountStrategy` interface
2. Add your strategy to the list in `ECommerceDiscountApp.java`
3. Write unit tests
4. Update documentation

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🎓 Educational Use

This project is perfect for:
- **Design pattern tutorials**
- **JavaFX development learning**
- **Software architecture demonstrations**
- **Code review sessions**
- **Programming course assignments**

## 🔗 Related Resources

- [Strategy Pattern - Gang of Four](https://en.wikipedia.org/wiki/Strategy_pattern)
- [JavaFX Documentation](https://openjfx.io/)
- [Maven Documentation](https://maven.apache.org/)
- [Java Design Patterns](https://java-design-patterns.com/)

---

**Built with ❤️ for education and demonstration purposes**