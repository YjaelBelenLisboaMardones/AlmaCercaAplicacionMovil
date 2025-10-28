Este es un excelente resumen para colocar en tu archivo README.md. Describe de manera profesional los objetivos, la funcionalidad y la arquitectura técnica implementada en el proyecto.

AlmaCercaApp: Plataforma de Comercio Local
1. Visión General del Proyecto
AlmaCercaApp es una aplicación móvil diseñada para conectar a los consumidores con los negocios locales de su vecindario. El proyecto se enfoca en establecer una arquitectura escalable y moderna en Jetpack Compose, permitiendo flujos de trabajo completamente separados para Compradores (clientes) y Vendedores (tiendas o negocios).

2. Funcionalidades y Flujo de Usuario
Este proyecto implementa una separación completa de roles desde el inicio hasta la gestión de datos:

A. Flujo de Autenticación y Registro
Selección de Rol: Tras el Onboarding, se introduce una pantalla RoleSelectionScreen que pregunta explícitamente al usuario si desea registrarse como Comprador o Vendedor.

Login Unificado: Ambas cuentas (BUYER y SELLER) comparten la misma pantalla de inicio de sesión (SignInScreen).

Redirección Condicional: Tras un inicio de sesión o registro exitoso, el sistema consulta el role del usuario en la base de datos y lo redirige al módulo principal correspondiente:

BUYER: Navega a MainScreen (con barra de navegación inferior).

SELLER: Navega a SellerMainScreen (con menú lateral/Drawer).

B. Módulo Comprador (Cliente)
Este módulo mantiene la navegación fluida con una barra inferior y contiene pantallas estándar como:

Home, Explorar, Carrito, Favoritos.

Secciones de soporte: FAQ, Centro de Ayuda, y Soporte/Chat.

C. Módulo Vendedor (Tienda) 🏪
Este módulo es una aplicación dentro de la aplicación, diseñada para gestionar el negocio:

Estructura: Utiliza una navegación jerárquica con un menú lateral (Drawer) en SellerMainScreen.

Menú Lateral (Funciones Implementadas):

Dashboard (Inicio): Resumen de ventas, estadísticas y acciones rápidas.

Mis Productos: Lista de productos activos.

Cerrar Sesión.

Gestión de Productos (CRUD): Implementación completa del ciclo de vida del producto en el frontend:

SellerProductsScreen: Muestra la lista de productos reales desde la base de datos.

AddEditProductScreen: Formulario unificado para agregar productos nuevos o editar productos existentes (distingue por el productId opcional en la ruta).

3. Arquitectura Técnica
El proyecto sigue patrones de diseño modernos de Android para garantizar la separación de intereses, el manejo robusto del estado y la persistencia de datos.

Arquitectura: Principios de Clean Architecture y MVVM (Model-View-ViewModel).

Persistencia de Datos: La información de usuarios y productos se almacena localmente utilizando Room Persistence Library (basada en SQLite).

Capa de Datos: Se han creado entidades (UserEntity, ProductEntity) y repositorios (UserRepository, ProductRepository) con sus respectivos DAOs (UserDao, ProductDao) para abstraer las operaciones de la base de datos.

Manejo del Estado: Se utiliza Kotlin Flow (StateFlow) en el AuthViewModel y SellerViewModel para exponer los datos de forma reactiva, asegurando que la interfaz de usuario (@Composable) se actualice automáticamente ante cambios en la base de datos (por ejemplo, al añadir o eliminar un producto).

Navegación: Se usa Jetpack Navigation Compose con gráficos anidados para gestionar la complejidad de las dos secciones principales (NavGraph principal -> HomeNavGraph / SellerNavGraph).

Inyección de Dependencias: Se utiliza ViewModelProvider.Factory para inyectar los repositorios (UserRepository, ProductRepository) en los ViewModels (AuthViewModel, SellerViewModel).
