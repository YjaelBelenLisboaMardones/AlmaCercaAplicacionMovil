# AlmaCercaApp: Plataforma de Comercio Local
## 1. Resumen del Proyecto
AlmaCercaApp es una aplicación móvil desarrollada en Jetpack Compose que establece una conexión directa y eficiente entre Compradores y Vendedores locales. El proyecto implementa una arquitectura robusta MVVM sobre Room (SQLite), destacando por su manejo de roles y la separación completa de flujos de trabajo.
Nombres: 
- Yjael Lisboa
- Marcelo Palma
## 2. Funcionalidades Implementadas
A. Autenticación y Flujo Condicional
Selección de Rol: El flujo de registro inicia con la pantalla RoleSelectionScreen para que el usuario elija su rol (BUYER o SELLER).

Login Unificado: El sistema consulta el rol guardado en la base de datos tras el login y redirige:

Comprador: Redirigido a MainScreen (Interfaz de Compra con Barra Inferior).

Vendedor: Redirigido a SellerMainScreen (Módulo de Gestión con Menú Lateral).

B. Módulo Vendedor (Gestión de Inventario)
El área del vendedor implementa la lógica de gestión de inventario:

CRUD de Productos: Funcionalidad para Agregar, Editar y Eliminar productos a través de AddEditProductScreen.

Soporte: Pantallas de FAQ, Centro de Ayuda y Soporte/Chat disponibles en el menú lateral.
## 3. Pasos para la Ejecución
Requisitos
-Android Studio (Versión reciente).
-SDK Mínimo: API 24 (Android 7.0).

-Ejecución y Verificación
Limpiar Proyecto: Vaya a Build > Clean Project. Sincronizar y Ejecutar
Verificar Persistencia: Use el Database Inspector de Android Studio para confirmar que las tablas users y guardan los datos correctamente.
