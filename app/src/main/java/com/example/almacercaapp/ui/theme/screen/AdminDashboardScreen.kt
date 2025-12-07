package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf("Inicio / Dashboard") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { AdminDrawerContent(currentScreen) { screen ->
            currentScreen = screen
            scope.launch { drawerState.close() }
        } }
    ) {
        AdminScaffoldContent(
            viewModel = viewModel,
            currentScreen = currentScreen,
            onMenuClick = { scope.launch { drawerState.open() } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminDrawerContent(currentScreen: String, onScreenSelected: (String) -> Unit) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Almacerca Admin", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp))
            NavigationDrawerItem(label = { Text("Inicio / Dashboard") }, selected = currentScreen == "Inicio / Dashboard", onClick = { onScreenSelected("Inicio / Dashboard") }, icon = { Icon(Icons.Default.Dashboard, null) }, modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding))
            NavigationDrawerItem(label = { Text("Gestión Productos") }, selected = currentScreen == "Gestión Productos", onClick = { onScreenSelected("Gestión Productos") }, icon = { Icon(Icons.Default.Inventory, null) }, modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminScaffoldContent(viewModel: AdminViewModel, currentScreen: String, onMenuClick: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showBottomSheet by remember { mutableStateOf(false) }
    val creationStatus by viewModel.creationStatus.collectAsState()
    val productToEdit by viewModel.productToEdit

    LaunchedEffect(productToEdit) {
        if (productToEdit != null) {
            showBottomSheet = true
        }
    }

    LaunchedEffect(creationStatus) {
        creationStatus?.let {
            if (it.contains("Éxito")) {
                snackbarHostState.showSnackbar(message = it)
                showBottomSheet = false
                viewModel.clearStatusMessage()
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(currentScreen) }, navigationIcon = { IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Abrir menú") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) },
        floatingActionButton = { if (currentScreen == "Gestión Productos") { FloatingActionButton(onClick = {
            viewModel.finishEditing()
            showBottomSheet = true
        }) { Icon(Icons.Default.Add, "Añadir Producto") } } },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (currentScreen) {
                "Inicio / Dashboard" -> DashboardHomeScreen()
                "Gestión Productos" -> ProductManagementScreen(viewModel = viewModel)
            }
            if (showBottomSheet) {
                ModalBottomSheet(onDismissRequest = {
                    showBottomSheet = false
                    viewModel.finishEditing() // Limpia el estado si el usuario descarta
                }, sheetState = sheetState) {
                    CreateProductForm(
                        viewModel = viewModel,
                        isEditing = productToEdit != null, // Pasa true si hay producto en edición
                        onDismiss = {
                            showBottomSheet = false
                            viewModel.finishEditing()
                        }
                    )
                }
            }
        }
    }
}

// --- PANTALLAS DE CONTENIDO ---

@Composable
fun DashboardHomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DashboardStatCard("Ventas Hoy", "$150.000", Icons.Default.MonetizationOn, MaterialTheme.colorScheme.tertiaryContainer)
        DashboardStatCard("Pedidos Pendientes", "5", Icons.Default.PendingActions, MaterialTheme.colorScheme.secondaryContainer)
        DashboardStatCard("Usuarios Nuevos", "12", Icons.Default.PersonAdd, MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable
fun ProductManagementScreen(viewModel: AdminViewModel) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading && products.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (products.isEmpty()) {
        ComingSoonScreen("No hay productos", "Crea el primero usando el botón '+'")
    } else {
        ProductList(
            products = products,
            onEdit = { product ->
                viewModel.startEditing(product) // Inicia el proceso de edición
            },
            onDelete = { product ->
                viewModel.deleteProduct(product) // Llama a la función de eliminación
            }
        )
    }
}

// --- COMPONENTES VISUALES ---

@Composable
fun DashboardStatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ComingSoonScreen(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Construction, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Text(subtitle, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}

@Composable
fun ProductList(
    products: List<ProductDto>,
    onEdit: (ProductDto) -> Unit,
    onDelete: (ProductDto) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(products) { product ->
            ProductRow(
                product = product,
                onEdit = { onEdit(product) },
                onDelete = { onDelete(product) }
            )
        }
    }
}

@Composable
fun ProductRow(product: ProductDto, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(product.imageUrl).crossfade(true).build(),
                contentDescription = "Imagen de ${product.name}",
                modifier = Modifier.size(64.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(String.format("$%.2f", product.price), color = MaterialTheme.colorScheme.primary)
            }
            // Botón de edición
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.secondary)
            }
            // Botón de eliminación
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun CreateProductForm(viewModel: AdminViewModel, isEditing: Boolean, onDismiss: () -> Unit) {
    val name by viewModel.productName
    val description by viewModel.productDescription
    val price by viewModel.productPrice
    val stock by viewModel.productStock
    val imageUrl by viewModel.productImageUrl
    val creationStatus by viewModel.creationStatus.collectAsState()

    val title = if (isEditing) "Editar Producto" else "Nuevo Producto"
    val buttonText = if (isEditing) "Guardar Cambios" else "Crear Producto"

    Column(modifier = Modifier.fillMaxWidth().padding(24.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = name, onValueChange = { viewModel.productName.value = it }, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = description, onValueChange = { viewModel.productDescription.value = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = price, onValueChange = { viewModel.productPrice.value = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = stock, onValueChange = { viewModel.productStock.value = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = imageUrl, onValueChange = { viewModel.productImageUrl.value = it }, label = { Text("URL de la imagen") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        creationStatus?.let {
            // Muestra indicador de carga solo si el estado es de "Creando" o "Actualizando"
            if (it.startsWith("Error")) {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
            } else if (it.contains("Creando") || it.contains("Actualizando")) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 8.dp))
            }
        }
        Button(
            onClick = {
                if (isEditing) {
                    viewModel.updateProduct() // Llama a la lógica de actualización
                } else {
                    viewModel.createProduct() // Llama a la lógica de creación
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = creationStatus?.contains("Creando") != true && creationStatus?.contains("Actualizando") != true
        ) {
            Text(buttonText)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
