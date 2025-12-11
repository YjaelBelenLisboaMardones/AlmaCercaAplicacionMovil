# AlmaCercaApp: Plataforma de Comercio Local
## 1. Resumen del Proyecto
AlmaCercaApp es una aplicación móvil desarrollada en Jetpack Compose que establece una conexión directa y eficiente entre Compradores y Vendedores locales. El proyecto implementa una arquitectura robusta MVVM, destacando por su manejo de roles y la separación completa de flujos de trabajo.
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

## Reporte de Estado Operacional Sistene Almacerca V1.0
El sistema AlmaCerca(MVP) ha avanzado a la fase de integración y testing. Se ha completado la migración hacia estabilización de la persistencia de datos y la planificación de la Infraestructura Cloud, utilizando MongoDB para la persistencia de datos. El servicio APIRest está preparado para soportar los dos flujos de negocios críticos: Compra y Gestión de Inventario(Admin).
El flujo ode productos en la experiencia de Comprador ha sido validado y los endpoints(GET/ Productos) responden satisfactoriamente. El próximo paso de desarrollo requiere la validación de la integridad de datos para la funcionalidad del Carrito de Compras.

## Incidente Abiero (P1) 
Fallo de consistencia en la Gestión de Inventario. Se ha detectado una disfunción de alto impacto operacional en el módulo de Administración. Aunque el flujo Post de creación de producto retorna un código de respuesta 201(CREATED), el producto no se refleja en la lista al consultar el endpoint GET. Esto compromete la continuidad operacional del administrador, impidiendo la gestión de stock(edición/eliminación). La hipótesis del Triage es un fallo en la lógica de filtrado de la query de lectura del servicio.
