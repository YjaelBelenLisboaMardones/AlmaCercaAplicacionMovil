# AlmaCercaApp: Plataforma de Comercio Local
## 1. Resumen del Proyecto
AlmaCercaApp es una aplicación móvil desarrollada en Jetpack Compose que establece una conexión directa y eficiente entre Compradores y Vendedores locales. El proyecto implementa una arquitectura robusta MVVM sobre Room (SQLite), destacando por su manejo de roles y la separación completa de flujos de trabajo.

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
3. Arquitectura y Pruebas del Sistema📋 Requisitos de EjecuciónIDE: Android Studio (Ladybug o versión reciente).
Versión Android: Mínimo API 24 (Android 7.0).Conectividad: Acceso a Internet (Requerido para conectar con Railway).

4. 🔗 Endpoints de la API (Backend)
La aplicación se conecta a un servidor desplegado en Railway. 
5. A continuación se detallan las rutas disponibles y los permisos necesarios para consumirlas.
Método HTTP   Ruta (Endpoint)  Rol RequeridoDescripción
POST/api/auth/register    PúblicoRegistra un nuevo usuario (comprador).
POST/api/auth/loginPúblicoInicia sesión y devuelve un objeto Usuario (con ID y Rol).
GET/api/admin/productsADMINLista todos los productos del sistema.
POST/api/admin/productsADMINAgrega un nuevo producto al catálogo.
PUT/api/admin/products/{id}ADMINModifica un producto existente por su ID.
DELETE/api/admin/products/{id}ADMINElimina un producto del catálogo.
GET/api/cartBUYERObtiene la lista de ítems del carrito del usuario actual.
POST/api/cartBUYERAgrega un ítem (producto + cantidad) al carrito.
DELETE/api/cart/items/{productId}BUYERElimina un ítem específico del carrito.
🔐 Nota de Seguridad: Para las rutas protegidas (ADMIN y BUYER), 
es obligatorio enviar el Header: userId: [ID_DEL_USUARIO].




## 📑 Reporte de Estado Operacional - AlmaCerca App

**Fecha:** 13 de Diciembre, 2025  
**Versión:** 1.0


### 📋 Resumen Ejecutivo
- Carrito de compras, Products, AdminProducts operativo end-to-end (backend y Android), con persistencia en MongoDB y sincronización en tiempo real.
- Estado general: ✅ OPERACIONAL CON ÉXITO.


### 📱 Frontend (Android/Kotlin)
- Arquitectura: MVVM + Repository Pattern, DI manual en MainActivity, DataStore para sesión, Retrofit/OkHttp.
- Repositorios/ViewModels: CartRepository como clase (suspend + backend sync), ProductDetailViewModel con onAddToCart(), CartViewModel y CheckoutViewModel funcionales, FavoritesRepository singleton en memoria.
- Pantallas: Login/Register, Productos, Detalle, Carrito, Checkout, Favoritos (local).
- Red: ApiService con firmas correctas y header userId agregado por AuthInterceptor.


✅ FUNCIONALIDADES COMPLETADAS

✅ Autenticación con tokens
✅ Listado y filtrado de productos
✅ Detalle de producto con imágenes
✅ Carrito funcionando (POST/GET/PUT/DELETE)
✅ Checkout con confirmación
✅ Sincronización backend-app
✅ Manejo de errores UI


### 🔌 Integración Frontend-Backend
- Flujo agregar al carrito: ProductDetailScreen → ViewModel.onAddToCart() → CartRepository.addProduct() → POST `/api/cart/add` con `userId` → MongoDB → estado local actualizado.
- DataStore: keys compartidas `PreferencesKeys.USER_ID` y `USER_ROLE` para UserRepository y CartRepository.


## 🛠️ Problemas Resueltos

| Problema | Causa | Solución |
| --- | --- | --- |
| userId NULL en CartRepository | Keys de DataStore con diferentes instancias | Crear PreferencesKeys.kt compartido |
| POST /api/cart no llegaba | onGoToCart() navegaba antes de completar | Remover navegación inmediata del botón |
| Form urlencoded vs JSON | EndPoint esperaba JSON | Agregar @RequestBody a CartController |
| ProductService.update() incompleto | No guardaba stock/imageUrl/categoryId | Agregar setters faltantes |
| Carrito solo local | CartRepository era object (singleton) | Convertir a class con DI |


### 🚀 Recomendaciones Next Sprint

- **Registrar compras del buyer en el panel de administración** para trazabilidad y consulta operativa.