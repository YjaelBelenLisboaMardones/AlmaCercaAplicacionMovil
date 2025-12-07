package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.almacercaapp.R
import com.example.almacercaapp.model.ProductCategory
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf("Gestión Productos") }

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

@Composable
private fun AdminDrawerContent(currentScreen: String, onScreenSelected: (String) -> Unit) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Almacerca Admin", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp))
            NavigationDrawerItem(label = { Text("Dashboard") }, selected = currentScreen == "Dashboard", onClick = { onScreenSelected("Dashboard") }, icon = { Icon(Icons.Default.Dashboard, null) })
            NavigationDrawerItem(label = { Text("Gestión Productos") }, selected = currentScreen == "Gestión Productos", onClick = { onScreenSelected("Gestión Productos") }, icon = { Icon(Icons.Default.Inventory, null) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminScaffoldContent(viewModel: AdminViewModel, currentScreen: String, onMenuClick: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    var showBottomSheet by remember { mutableStateOf(false) }
    val uiStatus by viewModel.uiStatus.collectAsState()

    LaunchedEffect(uiStatus) {
        uiStatus?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Short)
            if (it.contains("éxito")) { // Si el mensaje es de éxito, cierra el bottom sheet
                showBottomSheet = false
            }
            viewModel.uiStatus.value = null // Limpia el mensaje después de mostrarlo
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(currentScreen) }, navigationIcon = { IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Abrir menú") } }) },
        floatingActionButton = {
            if (currentScreen == "Gestión Productos") {
                FloatingActionButton(onClick = {
                    // Llama a la función que SÍ existe en la lógica antigua para limpiar el formulario
                    viewModel.resetProductFields()
                    showBottomSheet = true
                }) { Icon(Icons.Default.Add, "Añadir Producto") }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (currentScreen) {
                "Dashboard" -> DashboardHomeScreen()
                // La pantalla de gestión ya no necesita pasar las funciones de editar/eliminar
                "Gestión Productos" -> ProductManagementScreen(viewModel = viewModel)
            }

            if (showBottomSheet) {
                ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
                    // Usa un formulario que solo sabe cómo crear productos
                    CreateProductForm(viewModel = viewModel)
                }
            }

            // Se elimina el diálogo de confirmación porque la lógica ya no existe
        }
    }
}

// --- PANTALLAS DE CONTENIDO ---

@Composable
fun DashboardHomeScreen() {
    ComingSoonScreen("Dashboard en Construcción", "Próximamente: estadísticas de ventas y más.")
}

@Composable
fun ProductManagementScreen(viewModel: AdminViewModel) {
    val products by viewModel.products.collectAsState()

    if (products.isEmpty()) {
        ComingSoonScreen("No hay productos", "Crea el primero usando el botón '+'")
    } else {
        // La lista de productos ya no necesita los callbacks de onEditClick y onDeleteClick
        ProductList(products = products)
    }
}

// --- COMPONENTES VISUALES ---

@Composable
fun ProductList(products: List<ProductDto>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products, key = { it.id!! }) { product ->
            // La tarjeta ahora es más simple, solo muestra información
            AdminProductCard(product = product)
        }
    }
}

@Composable
fun AdminProductCard(product: ProductDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(product.imageUrl).crossfade(true).error(R.drawable.placeholder_image).build(),
                contentDescription = product.name,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Stock: ${product.stock}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                // Ya no hay botones de editar/eliminar aquí
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductForm(viewModel: AdminViewModel) {
    val name by viewModel.productName.collectAsState()
    val description by viewModel.productDescription.collectAsState()
    val price by viewModel.productPrice.collectAsState()
    val stock by viewModel.productStock.collectAsState()
    val imageUrl by viewModel.productImageUrl.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Nuevo Producto", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { viewModel.productName.value = it }, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(expanded = categoryMenuExpanded, onExpandedChange = { categoryMenuExpanded = !categoryMenuExpanded }) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "Selecciona una categoría",
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = categoryMenuExpanded, onDismissRequest = { categoryMenuExpanded = false }) {
                categories.forEach { category ->
                    DropdownMenuItem(text = { Text(category.name) }, onClick = {
                        viewModel.selectedCategory.value = category
                        categoryMenuExpanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = description, onValueChange = { viewModel.productDescription.value = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = price, onValueChange = { viewModel.productPrice.value = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = stock, onValueChange = { viewModel.productStock.value = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = imageUrl, onValueChange = { viewModel.productImageUrl.value = it }, label = { Text("URL de la imagen") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        // El botón solo llama a la función de guardar que SÍ existe en la lógica antigua
        Button(onClick = { viewModel.onSaveProduct() }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text("Crear Producto")
        }
        Spacer(modifier = Modifier.height(24.dp))
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
