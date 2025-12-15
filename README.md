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

**Fecha:** 15 de Diciembre, 2025  
Versión: 1.1 (Producción / Cloud Release) 
Servicio: 🟢 ONLINE / ESTABLE

📋 1. Resumen Ejecutivo
El sistema ha completado exitosamente su migración a un entorno de Producción en la Nube. La arquitectura ahora opera bajo un modelo distribuido, con el Backend contenerizado en la nube y el Frontend móvil consumiendo datos reales vía HTTPS.

Resultado: Conectividad End-to-End (E2E) verificada al 100%.

☁️ 2. Infraestructura y Despliegue

Servidor: Render (Docker Container Runtime).

Base de Datos: MongoDB Atlas (Cluster Producción). Conexión segura establecida.

Seguridad: Credenciales inyectadas vía Variables de Entorno (ENV VARS). Ninguna credencial sensible expuesta en el repositorio.

Red: Configuración de puerto explícita (PORT=8080) para resolución de Health Checks externos.

✅ 3. Módulos y Endpoints Verificados

Se ha realizado una batería de pruebas de integración (Postman + Android) confirmando la operatividad de los siguientes módulos:

🔐 Autenticación y Usuarios:

Login y Registro (/api/auth/*) funcionales. Generación de JWT Tokens correcta.
Roles de usuario (BUYER/ADMIN) persistidos en MongoDB.

📦 Catálogo de Productos:

Listado general y detalle (/api/products/*) sirviendo datos en tiempo real.
Imágenes y stock sincronizados.

🛒 Carrito de Compras:

Flujo completo operativo: Agregar, Eliminar, Actualizar cantidad y Vaciar carrito.
Persistencia de carrito por userId verificada.

🛡️ Administración:

Endpoints de gestión (/api/admin/*) activos y protegidos por rol.

🛠️ 4. Registro de Incidentes y Resolución (Troubleshooting Log)

A continuación, se detalla el historial de incidentes resueltos durante el pase a producción para referencia futura del equipo de desarrollo:

🔴 Severidad: Crítica | Riesgo de Fuga de Credenciales

Causa Raíz: Archivo .gitignore mal ubicado (dentro de carpeta .vscode), dejando expuesto launch.json.
Solución: Reubicación del archivo a la raíz del proyecto y limpieza de caché de Git. Validación exitosa con git status.

🟠 Severidad: Alta | Fallo de Despliegue en Render

Causa Raíz: Error "No open ports detected". Render no detectaba el arranque de Spring Boot dentro del tiempo límite.
Solución: Inyección de variable de entorno PORT=8080 en el dashboard de Render para forzar el enlace de puertos.

🟡 Severidad: Media | Error 403 Forbidden en Navegador

Causa Raíz: Intento de validación de endpoints POST (Login) usando el navegador (que envía GET).
Solución: Validación técnica realizada vía Postman. Endpoint responde correctamente con Token.

🟡 Severidad: Media | Timeout en Aplicación Móvil

Causa Raíz: Fenómeno de "Cold Start" (Arranque en frío). El servidor gratuito de Render entra en suspensión tras inactividad.
Solución: Se documentó el comportamiento esperado: latencia de ~60 segundos en la primera petición tras inactividad. Las peticiones subsiguientes son inmediatas.

⚪ Severidad: Baja | Conflicto en Control de Versiones

Causa Raíz: Divergencia entre la historia de la rama master remota y local.
Solución: Implementación de rama de contingencia feature/config-produccion para asegurar el despliegue sin forzar un merge riesgoso.✅ FUNCIONALIDADES COMPLETADAS



Funcionalidades Completadas:

✅ Autenticación con tokens
✅ Listado y filtrado de productos
✅ Detalle de producto con imágenes
✅ Carrito funcionando (POST/GET/PUT/DELETE)
✅ Checkout con confirmación
✅ Sincronización backend-app
✅ Manejo de errores UI


