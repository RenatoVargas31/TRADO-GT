-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: trado_db
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'tecnología'),(2,'muebles'),(3,'ropa');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `idChat` int NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int NOT NULL,
  `Orden_idOrden` int NOT NULL,
  `Mensaje` varchar(45) NOT NULL,
  `Tiempo` int NOT NULL,
  PRIMARY KEY (`idChat`),
  KEY `fk_Chat_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Chat_Orden1_idx` (`Orden_idOrden`),
  CONSTRAINT `fk_Chat_Orden1` FOREIGN KEY (`Orden_idOrden`) REFERENCES `orden` (`idOrden`),
  CONSTRAINT `fk_Chat_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chatbot`
--

DROP TABLE IF EXISTS `chatbot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chatbot` (
  `idChatBot` int NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int NOT NULL,
  `Mensaje` varchar(45) NOT NULL,
  `Respuesta` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idChatBot`),
  KEY `fk_ChatBot_Usuario1_idx` (`Usuario_idUsuario`),
  CONSTRAINT `fk_ChatBot_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chatbot`
--

LOCK TABLES `chatbot` WRITE;
/*!40000 ALTER TABLE `chatbot` DISABLE KEYS */;
/*!40000 ALTER TABLE `chatbot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `codigodespachador`
--

DROP TABLE IF EXISTS `codigodespachador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `codigodespachador` (
  `idCodigoDespachador` int NOT NULL AUTO_INCREMENT,
  `Caracteres` varchar(45) NOT NULL,
  `EstadoCodigo_idEstadoCodigo` int NOT NULL,
  `zona_idZona` int NOT NULL,
  PRIMARY KEY (`idCodigoDespachador`),
  KEY `fk_CodigoDespachador_EstadoCodigo1_idx` (`EstadoCodigo_idEstadoCodigo`),
  KEY `fk_codigodespachador_zona1_idx` (`zona_idZona`),
  CONSTRAINT `fk_CodigoDespachador_EstadoCodigo1` FOREIGN KEY (`EstadoCodigo_idEstadoCodigo`) REFERENCES `estadocodigo` (`idEstadoCodigo`),
  CONSTRAINT `fk_codigodespachador_zona1` FOREIGN KEY (`zona_idZona`) REFERENCES `zona` (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `codigodespachador`
--

LOCK TABLES `codigodespachador` WRITE;
/*!40000 ALTER TABLE `codigodespachador` DISABLE KEYS */;
INSERT INTO `codigodespachador` VALUES (1,'DPC-09067',1,1),(2,'DPC-10078',1,2),(3,'DPC-11089',1,3),(4,'DPC-12090',1,4),(5,'DPC-13001',1,1),(6,'DPC-14012',1,2),(7,'DPC-15023',1,3);
/*!40000 ALTER TABLE `codigodespachador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `distrito`
--

DROP TABLE IF EXISTS `distrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `distrito` (
  `idDistrito` int NOT NULL AUTO_INCREMENT,
  `Zona_idZona` int NOT NULL,
  `Codigo` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idDistrito`),
  KEY `fk_Distrito_Zona1_idx` (`Zona_idZona`),
  CONSTRAINT `fk_Distrito_Zona1` FOREIGN KEY (`Zona_idZona`) REFERENCES `zona` (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `distrito`
--

LOCK TABLES `distrito` WRITE;
/*!40000 ALTER TABLE `distrito` DISABLE KEYS */;
INSERT INTO `distrito` VALUES (1,1,'01','Ancon'),(2,1,'02','Santa Rosa'),(3,1,'03','Carabayllo'),(4,1,'04','Puente Piedra'),(5,1,'05','Comas'),(6,1,'06','Los Olivos'),(7,1,'07','San Martín de Porres'),(8,1,'08','Independencia'),(9,2,'09','San Juan de Miraflores'),(10,2,'10','Villa María del Triunfo'),(11,2,'11','Villa el Salvador'),(12,2,'12','Pachacamac'),(13,2,'13','Lurin'),(14,2,'14','Punta Hermosa'),(15,2,'15','Punta Negra'),(16,2,'16','San Bartolo'),(17,2,'17','Santa María del Mar'),(18,2,'18','Pucusana'),(19,3,'19','San Juan de Lurigancho'),(20,3,'20','Lurigancho/Chosica'),(21,3,'21','Ate'),(22,3,'22','El Agustino'),(23,3,'23','Santa Anita'),(24,3,'24','La Molina'),(25,3,'25','Cieneguilla'),(26,4,'26','Rimac'),(27,4,'27','Cercado de Lima'),(28,4,'28','Breña'),(29,4,'29','Pueblo Libre'),(30,4,'30','Magdalena'),(31,4,'31','Jesus María'),(32,4,'32','La Victoria'),(33,4,'33','Lince'),(34,4,'34','San Isidro'),(35,4,'35','San Miguel'),(36,4,'36','Surquillo'),(37,4,'37','San Borja'),(38,4,'38','Santiago de Surco'),(39,4,'39','Barranco'),(40,4,'40','Chorrillos'),(41,4,'41','San Luis'),(42,4,'42','Miraflores');
/*!40000 ALTER TABLE `distrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadocodigo`
--

DROP TABLE IF EXISTS `estadocodigo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadocodigo` (
  `idEstadoCodigo` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstadoCodigo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadocodigo`
--

LOCK TABLES `estadocodigo` WRITE;
/*!40000 ALTER TABLE `estadocodigo` DISABLE KEYS */;
INSERT INTO `estadocodigo` VALUES (1,'Habilitado'),(2,'Multado'),(3,'Cancelado'),(4,'Suspendido'),(5,'Anulado');
/*!40000 ALTER TABLE `estadocodigo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadoordenagente`
--

DROP TABLE IF EXISTS `estadoordenagente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadoordenagente` (
  `idEstadoOrdenAgente` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstadoOrdenAgente`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadoordenagente`
--

LOCK TABLES `estadoordenagente` WRITE;
/*!40000 ALTER TABLE `estadoordenagente` DISABLE KEYS */;
INSERT INTO `estadoordenagente` VALUES (1,'Sin asignar'),(2,'Pendiente'),(3,'En proceso'),(4,'Resuelto');
/*!40000 ALTER TABLE `estadoordenagente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadoordenimportador`
--

DROP TABLE IF EXISTS `estadoordenimportador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadoordenimportador` (
  `idEstadoOrdenImportador` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idEstadoOrdenImportador`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadoordenimportador`
--

LOCK TABLES `estadoordenimportador` WRITE;
/*!40000 ALTER TABLE `estadoordenimportador` DISABLE KEYS */;
INSERT INTO `estadoordenimportador` VALUES (1,'Creado'),(2,'En validación'),(3,'En proceso'),(4,'Arribo al país'),(5,'En aduanas'),(6,'En ruta'),(7,'Recibido');
/*!40000 ALTER TABLE `estadoordenimportador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacion`
--

DROP TABLE IF EXISTS `notificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacion` (
  `idNotificacion` int NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int NOT NULL,
  `Contenido` varchar(45) NOT NULL,
  PRIMARY KEY (`idNotificacion`),
  KEY `fk_Notificacion_Usuario1_idx` (`Usuario_idUsuario`),
  CONSTRAINT `fk_Notificacion_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacion`
--

LOCK TABLES `notificacion` WRITE;
/*!40000 ALTER TABLE `notificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden`
--

DROP TABLE IF EXISTS `orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden` (
  `idOrden` int NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int NOT NULL,
  `AgentCompra_idUsuario` int DEFAULT NULL,
  `Valoracion_idValoracion` int DEFAULT NULL,
  `EstadoOrdenAgente_idEstadoOrdenAgente` int DEFAULT NULL,
  `EstadoOrdenImportador_idEstadoOrdenImportador` int DEFAULT NULL,
  `FechaArribo` timestamp NOT NULL,
  `FechaCreacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `isDeleted` tinyint DEFAULT '0',
  PRIMARY KEY (`idOrden`),
  KEY `fk_Orden_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Orden_Usuario2_idx` (`AgentCompra_idUsuario`),
  KEY `fk_Orden_Valoracion1_idx` (`Valoracion_idValoracion`),
  KEY `fk_Orden_EstadoOrdenAgente1_idx` (`EstadoOrdenAgente_idEstadoOrdenAgente`),
  KEY `fk_Orden_EstadoOrdenImportador1_idx` (`EstadoOrdenImportador_idEstadoOrdenImportador`),
  CONSTRAINT `fk_Orden_EstadoOrdenAgente1` FOREIGN KEY (`EstadoOrdenAgente_idEstadoOrdenAgente`) REFERENCES `estadoordenagente` (`idEstadoOrdenAgente`),
  CONSTRAINT `fk_Orden_EstadoOrdenImportador1` FOREIGN KEY (`EstadoOrdenImportador_idEstadoOrdenImportador`) REFERENCES `estadoordenimportador` (`idEstadoOrdenImportador`),
  CONSTRAINT `fk_Orden_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`),
  CONSTRAINT `fk_Orden_Usuario2` FOREIGN KEY (`AgentCompra_idUsuario`) REFERENCES `usuario` (`idUsuario`),
  CONSTRAINT `fk_Orden_Valoracion1` FOREIGN KEY (`Valoracion_idValoracion`) REFERENCES `valoracion` (`idValoracion`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden`
--

LOCK TABLES `orden` WRITE;
/*!40000 ALTER TABLE `orden` DISABLE KEYS */;
INSERT INTO `orden` VALUES (1,8,NULL,NULL,1,1,'2024-09-30 10:57:48','2024-10-02 05:43:55',0),(2,9,1,NULL,2,2,'2024-09-30 10:57:48','2024-10-02 05:43:55',1),(3,10,1,NULL,3,3,'2024-09-30 10:57:48','2024-10-02 05:43:55',0),(4,11,1,NULL,3,4,'2024-09-30 10:57:48','2024-10-02 05:43:55',0),(5,12,1,NULL,4,7,'2024-09-30 10:57:48','2024-10-02 05:43:55',0),(6,8,1,NULL,3,5,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(7,9,1,NULL,3,6,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(8,10,NULL,NULL,1,1,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(9,11,1,NULL,2,2,'2024-09-30 10:59:19','2024-10-02 05:43:55',1),(10,12,1,NULL,3,4,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(11,8,1,NULL,4,7,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(12,9,NULL,NULL,1,1,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(13,10,1,NULL,3,3,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(14,11,1,NULL,3,6,'2024-09-30 10:59:19','2024-10-02 05:43:55',0),(15,12,1,NULL,2,2,'2024-09-30 10:59:19','2024-10-02 05:43:55',1);
/*!40000 ALTER TABLE `orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `idPago` int NOT NULL AUTO_INCREMENT,
  `Orden_idOrden` int NOT NULL,
  `Metodo` varchar(45) NOT NULL,
  `Monto` decimal(10,2) NOT NULL,
  `Fecha` timestamp NOT NULL,
  PRIMARY KEY (`idPago`),
  KEY `fk_Pago_Orden1_idx` (`Orden_idOrden`),
  CONSTRAINT `fk_Pago_Orden1` FOREIGN KEY (`Orden_idOrden`) REFERENCES `orden` (`idOrden`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
INSERT INTO `pago` VALUES (1,1,'Tarjeta de crédito',150.00,'2024-09-30 11:33:30'),(2,2,'Transferencia bancaria',200.75,'2024-09-30 11:33:30'),(3,3,'PayPal',50.99,'2024-09-30 11:33:30'),(4,4,'Tarjeta de débito',300.50,'2024-09-30 11:33:30'),(5,5,'Efectivo',100.00,'2024-09-30 11:33:30'),(6,6,'Tarjeta de crédito',175.00,'2024-09-30 11:33:30'),(7,7,'Transferencia bancaria',220.10,'2024-09-30 11:33:30'),(8,8,'PayPal',75.45,'2024-09-30 11:33:30'),(9,9,'Tarjeta de débito',130.30,'2024-09-30 11:33:30'),(10,10,'Efectivo',60.20,'2024-09-30 11:33:30'),(11,11,'Tarjeta de crédito',255.00,'2024-09-30 11:33:30'),(12,12,'Transferencia bancaria',310.00,'2024-09-30 11:33:30'),(13,13,'PayPal',99.99,'2024-09-30 11:33:30'),(14,14,'Tarjeta de débito',85.75,'2024-09-30 11:33:30'),(15,15,'Efectivo',150.50,'2024-09-30 11:33:30');
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pregunta`
--

DROP TABLE IF EXISTS `pregunta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pregunta` (
  `idPregunta` int NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int NOT NULL,
  `Contenido` varchar(80) NOT NULL,
  `Fecha` timestamp NOT NULL,
  PRIMARY KEY (`idPregunta`),
  KEY `fk_Consulta_Usuario1_idx` (`Usuario_idUsuario`),
  CONSTRAINT `fk_Consulta_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pregunta`
--

LOCK TABLES `pregunta` WRITE;
/*!40000 ALTER TABLE `pregunta` DISABLE KEYS */;
/*!40000 ALTER TABLE `pregunta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `Proveedor_idProveedor` int NOT NULL,
  `SubCategoria_idSubCategoria` int NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Cantidad` varchar(45) NOT NULL,
  `Descripcion` varchar(45) NOT NULL,
  `Precio` varchar(45) NOT NULL,
  `Color` varchar(45) DEFAULT NULL,
  `Talla` varchar(45) DEFAULT NULL,
  `Material` varchar(45) DEFAULT NULL,
  `Modelo` varchar(45) DEFAULT NULL,
  `Resolucion` varchar(45) DEFAULT NULL,
  `Ram` varchar(45) DEFAULT NULL,
  `Almacenamiento` varchar(45) DEFAULT NULL,
  `isDeleted` tinyint DEFAULT '0',
  PRIMARY KEY (`idProducto`),
  KEY `fk_Producto_Proveedor1_idx` (`Proveedor_idProveedor`),
  KEY `fk_Producto_SubCategoria1_idx` (`SubCategoria_idSubCategoria`),
  CONSTRAINT `fk_Producto_Proveedor1` FOREIGN KEY (`Proveedor_idProveedor`) REFERENCES `proveedor` (`idProveedor`),
  CONSTRAINT `fk_Producto_SubCategoria1` FOREIGN KEY (`SubCategoria_idSubCategoria`) REFERENCES `subcategoria` (`idSubCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,1,1,'Laptop Azer RP45','100','laptop gamer para pros','5000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(2,1,2,'Air pods pro 4','200','buenos audifonos se escucha xd','3000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(3,2,3,'Monitor ASUS','300','GAAAAA juega dotita','4000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(4,3,4,'Mueble grandecito pato','400','patototototo','2000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(5,3,5,'Cuarto completo','500','vamos al cuarto bb','10000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(6,4,6,'Silla potona','600','dsadsadsadsa','3000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(7,4,7,'Polo boxy fit','700','dadsadsadsa','5000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(8,5,8,'Falda','800','wereretretb','9000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(9,5,9,'overol','900','psdsadsa','12309',NULL,NULL,NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productoenzona`
--

DROP TABLE IF EXISTS `productoenzona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productoenzona` (
  `Producto_idProducto` int NOT NULL,
  `Zona_idZona` int NOT NULL,
  `Cantidad` int NOT NULL,
  `CostoEnvio` decimal(10,2) NOT NULL,
  `estadoRepo` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`Producto_idProducto`,`Zona_idZona`),
  KEY `fk_Producto_has_Zona_Zona1_idx` (`Zona_idZona`),
  KEY `fk_Producto_has_Zona_Producto1_idx` (`Producto_idProducto`),
  CONSTRAINT `fk_Producto_has_Zona_Producto1` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `fk_Producto_has_Zona_Zona1` FOREIGN KEY (`Zona_idZona`) REFERENCES `zona` (`idZona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productoenzona`
--

LOCK TABLES `productoenzona` WRITE;
/*!40000 ALTER TABLE `productoenzona` DISABLE KEYS */;
INSERT INTO `productoenzona` VALUES (1,1,50,15.00,0),(1,2,30,12.00,0),(1,3,20,18.00,0),(1,4,25,20.00,0),(2,1,100,10.00,0),(2,2,80,8.00,0),(2,3,60,9.50,0),(2,4,70,11.00,0),(3,1,40,25.00,0),(3,2,35,22.00,0),(3,3,45,24.50,0),(3,4,50,23.00,0),(4,1,15,50.00,0),(4,2,10,48.00,0),(4,3,8,52.00,0),(4,4,20,49.00,0),(5,1,5,100.00,0),(5,2,6,105.00,0),(5,3,7,110.00,0),(5,4,4,120.00,0),(6,1,30,35.00,0),(6,2,20,32.00,0),(6,3,25,33.50,0),(6,4,28,34.00,0),(7,1,200,5.00,0),(7,2,180,4.50,0),(7,3,160,5.50,0),(7,4,170,6.00,0),(8,1,100,8.00,0),(8,2,90,7.50,0),(8,3,85,8.50,0),(8,4,110,9.00,0),(9,1,120,20.00,0),(9,2,100,19.00,0),(9,3,110,21.00,0),(9,4,130,22.00,0);
/*!40000 ALTER TABLE `productoenzona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productoenzonaenorden`
--

DROP TABLE IF EXISTS `productoenzonaenorden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productoenzonaenorden` (
  `ProductoEnZona_Producto_idProducto` int NOT NULL,
  `ProductoEnZona_Zona_idZona` int NOT NULL,
  `Orden_idOrden` int NOT NULL,
  `Cantidad` int NOT NULL,
  PRIMARY KEY (`ProductoEnZona_Producto_idProducto`,`ProductoEnZona_Zona_idZona`,`Orden_idOrden`),
  KEY `fk_ProductoEnZona_has_Orden_Orden1_idx` (`Orden_idOrden`),
  KEY `fk_ProductoEnZona_has_Orden_ProductoEnZona1_idx` (`ProductoEnZona_Producto_idProducto`,`ProductoEnZona_Zona_idZona`),
  CONSTRAINT `fk_ProductoEnZona_has_Orden_Orden1` FOREIGN KEY (`Orden_idOrden`) REFERENCES `orden` (`idOrden`),
  CONSTRAINT `fk_ProductoEnZona_has_Orden_ProductoEnZona1` FOREIGN KEY (`ProductoEnZona_Producto_idProducto`, `ProductoEnZona_Zona_idZona`) REFERENCES `productoenzona` (`Producto_idProducto`, `Zona_idZona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productoenzonaenorden`
--

LOCK TABLES `productoenzonaenorden` WRITE;
/*!40000 ALTER TABLE `productoenzonaenorden` DISABLE KEYS */;
INSERT INTO `productoenzonaenorden` VALUES (1,1,1,10),(1,2,4,8),(1,2,10,20),(1,3,2,9),(1,4,6,6),(2,1,8,11),(2,1,12,6),(2,2,2,5),(2,3,1,6),(2,3,11,25),(2,4,3,4),(3,1,14,3),(3,2,1,7),(3,3,3,8),(3,4,5,9),(3,4,9,4),(3,4,12,30),(4,1,2,2),(4,1,13,5),(4,3,10,3),(4,4,4,3),(5,1,5,1),(5,1,11,6),(5,2,7,2),(5,2,14,1),(5,2,15,2),(5,3,3,2),(6,2,6,2),(6,2,13,7),(6,3,8,3),(6,3,15,4),(7,2,9,12),(7,3,7,12),(7,3,15,10),(7,4,4,10),(7,4,12,4),(8,3,6,7),(8,3,11,9),(8,4,8,9),(8,4,14,5),(9,1,9,15),(9,2,5,5),(9,2,10,7),(9,3,7,8),(9,3,13,4);
/*!40000 ALTER TABLE `productoenzonaenorden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `idProveedor` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Telefono` varchar(9) NOT NULL,
  `Ruc` varchar(45) NOT NULL,
  `Dni` varchar(8) NOT NULL,
  `Tienda` varchar(45) NOT NULL,
  `isDeleted` tinyint DEFAULT '0',
  PRIMARY KEY (`idProveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
INSERT INTO `proveedor` VALUES (1,'Proveedor 1','987654321','12345678901','12345678','Tienda 1',0),(2,'Proveedor 2','987654322','12345678902','12345679','Tienda 2',0),(3,'Proveedor 3','987654323','12345678903','12345680','Tienda 3',0),(4,'Proveedor 4','987654324','12345678904','12345681','Tienda 4',0),(5,'Proveedor 5','987654325','12345678905','12345682','Tienda 5',0);
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resena`
--

DROP TABLE IF EXISTS `resena`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resena` (
  `idResena` int NOT NULL AUTO_INCREMENT,
  `Foto` varchar(45) NOT NULL,
  `Comentario` varchar(200) NOT NULL,
  `Calidad` varchar(45) NOT NULL,
  `TiempoEntrega` varchar(45) NOT NULL,
  `Producto_idProducto` int NOT NULL,
  `Usuario_idUsuario` int NOT NULL,
  PRIMARY KEY (`idResena`),
  KEY `fk_Resena_Producto1_idx` (`Producto_idProducto`),
  KEY `fk_Resena_Usuario1_idx` (`Usuario_idUsuario`),
  CONSTRAINT `fk_Resena_Producto1` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `fk_Resena_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resena`
--

LOCK TABLES `resena` WRITE;
/*!40000 ALTER TABLE `resena` DISABLE KEYS */;
/*!40000 ALTER TABLE `resena` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `idRol` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idRol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'SuperAdmin'),(2,'Administrador Zonal'),(3,'Agente de Compra'),(4,'Usuario Final');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategoria`
--

DROP TABLE IF EXISTS `subcategoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategoria` (
  `idSubCategoria` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Categoria_idCategoria` int NOT NULL,
  PRIMARY KEY (`idSubCategoria`),
  KEY `fk_SubCategoria_Categoria1_idx` (`Categoria_idCategoria`),
  CONSTRAINT `fk_SubCategoria_Categoria1` FOREIGN KEY (`Categoria_idCategoria`) REFERENCES `categoria` (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategoria`
--

LOCK TABLES `subcategoria` WRITE;
/*!40000 ALTER TABLE `subcategoria` DISABLE KEYS */;
INSERT INTO `subcategoria` VALUES (1,'Laptops',1),(2,'Audífonos',1),(3,'Monitores',1),(4,'Cómodas',2),(5,'Roperos',2),(6,'Sillas',2),(7,'Hombre',3),(8,'Mujer',3),(9,'Niñes',3);
/*!40000 ALTER TABLE `subcategoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `idToken` int NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` int NOT NULL,
  `Tipo` varchar(45) NOT NULL,
  `Duracion` int NOT NULL,
  `FechaCreacion` timestamp NOT NULL,
  PRIMARY KEY (`idToken`),
  KEY `fk_Token_Usuario1_idx` (`Usuario_idUsuario`),
  CONSTRAINT `fk_Token_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `Rol_idRol` int NOT NULL,
  `AdmZonal_idUsuario` int DEFAULT NULL,
  `AgentCompra_idUsuario` int DEFAULT NULL,
  `Zona_idZona` int DEFAULT NULL,
  `Distrito_idDistrito` int DEFAULT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Apellido` varchar(45) NOT NULL,
  `FechaNacimiento` date DEFAULT NULL,
  `Dni` varchar(8) DEFAULT NULL,
  `Telefono` varchar(9) DEFAULT NULL,
  `Correo` varchar(45) DEFAULT NULL,
  `Contrasena` varchar(45) DEFAULT NULL,
  `Ruc` varchar(45) DEFAULT NULL,
  `RazonSocial` varchar(45) DEFAULT NULL,
  `Direccion` varchar(45) DEFAULT NULL,
  `isAccepted` tinyint DEFAULT '0',
  `isPostulated` tinyint DEFAULT '0',
  `isActivated` tinyint DEFAULT '0',
  `Foto` varchar(45) DEFAULT NULL,
  `MotivoBanneo` varchar(45) DEFAULT NULL,
  `FechaBanneo` timestamp NULL DEFAULT NULL,
  `codigodespachador_idCodigoDespachador` int DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  KEY `fk_Usuario_Rol_idx` (`Rol_idRol`),
  KEY `fk_Usuario_Zona1_idx` (`Zona_idZona`),
  KEY `fk_Usuario_Distrito1_idx` (`Distrito_idDistrito`),
  KEY `fk_Usuario_Usuario1_idx` (`AdmZonal_idUsuario`),
  KEY `fk_Usuario_Usuario2_idx` (`AgentCompra_idUsuario`),
  KEY `fk_usuario_codigodespachador1_idx` (`codigodespachador_idCodigoDespachador`),
  CONSTRAINT `fk_usuario_codigodespachador1` FOREIGN KEY (`codigodespachador_idCodigoDespachador`) REFERENCES `codigodespachador` (`idCodigoDespachador`),
  CONSTRAINT `fk_Usuario_Distrito1` FOREIGN KEY (`Distrito_idDistrito`) REFERENCES `distrito` (`idDistrito`),
  CONSTRAINT `fk_Usuario_Rol` FOREIGN KEY (`Rol_idRol`) REFERENCES `rol` (`idRol`),
  CONSTRAINT `fk_Usuario_Usuario1` FOREIGN KEY (`AdmZonal_idUsuario`) REFERENCES `usuario` (`idUsuario`),
  CONSTRAINT `fk_Usuario_Usuario2` FOREIGN KEY (`AgentCompra_idUsuario`) REFERENCES `usuario` (`idUsuario`),
  CONSTRAINT `fk_Usuario_Zona1` FOREIGN KEY (`Zona_idZona`) REFERENCES `zona` (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,3,NULL,NULL,1,1,'José','Fernández','1986-03-20','12457836','987654311','jose.fernandez@example.com','password123','12457896301','Fernández Shipping S.A.C.','Av. Los Olivos 999',1,0,1,'foto6.jpg',NULL,NULL,1),(2,3,NULL,NULL,2,9,'Laura','Hidalgo','1991-07-11','98765432','912345634','laura.hidalgo@example.com','password456','10982345621','Hidalgo Imports Ltda.','Av. San Juan 456',1,0,1,'foto7.jpg',NULL,NULL,2),(3,3,NULL,NULL,3,19,'Pedro','Martínez','1989-02-10','56871234','912345674','pedro.martinez@example.com','password789','11234567543','Martínez Exports','Av. Lurigancho 1234',1,0,1,'foto8.jpg',NULL,NULL,3),(4,3,NULL,NULL,4,30,'Carmen','Silva','1993-06-15','12563478','987654332','carmen.silva@example.com','password987','11187654321','Silva Importaciones','Calle Magdalena 678',1,0,1,'foto9.jpg',NULL,NULL,4),(5,3,NULL,NULL,1,5,'Mario','Quispe','1985-10-25','23657891','934567125','mario.quispe@example.com','password654','22334455667','Quispe Trade S.A.C','Av. Comas 987',1,0,1,'foto10.jpg',NULL,NULL,5),(6,3,NULL,NULL,2,10,'Elena','Lopez','1984-11-13','18765432','987612345','elena.lopez@example.com','password321','10982345678','Lopez Trading S.A.C','Av. Villa María 456',1,0,1,'foto11.jpg',NULL,NULL,6),(7,3,NULL,NULL,3,21,'Sergio','Guzmán','1990-09-09','12657834','987654999','sergio.guzman@example.com','password159','22334412345','Guzmán Shipping Ltd.','Av. Ate 123',1,0,1,'foto12.jpg',NULL,NULL,7),(8,4,NULL,1,1,1,'Miguel','Pérez',NULL,'98765431','990024063','miguel.perez@example.com',NULL,'10093414992','PedritoCompany','Av. Los Olivos 999',0,0,1,NULL,NULL,NULL,NULL),(9,4,NULL,1,1,9,'Laura','Jiménez',NULL,'87654321',NULL,'laura.jimenez@example.com',NULL,NULL,NULL,'Av. San Juan 987',0,0,1,NULL,NULL,NULL,NULL),(10,4,NULL,1,1,19,'Jorge','Santos',NULL,'76543219',NULL,'jorge.santos@example.com',NULL,NULL,NULL,'Av. Lurigancho 345',0,0,1,NULL,NULL,NULL,NULL),(11,4,NULL,1,1,30,'Elena','García',NULL,'65432189',NULL,'elena.garcia@example.com',NULL,NULL,NULL,'Calle Magdalena 111',0,0,1,NULL,NULL,NULL,NULL),(12,4,NULL,1,1,5,'Raúl','Quispe',NULL,'54321897',NULL,'raul.quispe@example.com',NULL,NULL,NULL,'Av. Comas 654',0,0,1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuariorespuesta`
--

DROP TABLE IF EXISTS `usuariorespuesta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuariorespuesta` (
  `Pregunta_idPregunta` int NOT NULL,
  `Usuario_idUsuario` int NOT NULL,
  `Contenido` varchar(100) NOT NULL,
  `Fecha` timestamp NOT NULL,
  PRIMARY KEY (`Pregunta_idPregunta`,`Usuario_idUsuario`),
  KEY `fk_Pregunta_has_Usuario_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Pregunta_has_Usuario_Pregunta1_idx` (`Pregunta_idPregunta`),
  CONSTRAINT `fk_Pregunta_has_Usuario_Pregunta1` FOREIGN KEY (`Pregunta_idPregunta`) REFERENCES `pregunta` (`idPregunta`),
  CONSTRAINT `fk_Pregunta_has_Usuario_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuariorespuesta`
--

LOCK TABLES `usuariorespuesta` WRITE;
/*!40000 ALTER TABLE `usuariorespuesta` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuariorespuesta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `valoracion`
--

DROP TABLE IF EXISTS `valoracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `valoracion` (
  `idValoracion` int NOT NULL AUTO_INCREMENT,
  `Valor` varchar(45) NOT NULL,
  PRIMARY KEY (`idValoracion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `valoracion`
--

LOCK TABLES `valoracion` WRITE;
/*!40000 ALTER TABLE `valoracion` DISABLE KEYS */;
/*!40000 ALTER TABLE `valoracion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zona`
--

DROP TABLE IF EXISTS `zona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zona` (
  `idZona` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zona`
--

LOCK TABLES `zona` WRITE;
/*!40000 ALTER TABLE `zona` DISABLE KEYS */;
INSERT INTO `zona` VALUES (1,'Norte'),(2,'Sur'),(3,'Este'),(4,'Oeste');
/*!40000 ALTER TABLE `zona` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-02 18:17:45
