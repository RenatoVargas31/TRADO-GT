CREATE DATABASE  IF NOT EXISTS `proyecto_gtics` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `proyecto_gtics`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: proyecto_gtics
-- ------------------------------------------------------
-- Server version	8.0.35

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
  `nombreCategoria` varchar(45) NOT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `idMensajes` int NOT NULL AUTO_INCREMENT,
  `orden_idOrden` varchar(45) NOT NULL,
  `mensaje` varchar(500) NOT NULL,
  `fechaCreacion` datetime NOT NULL,
  `usuarios_idUsuarios` int NOT NULL,
  PRIMARY KEY (`idMensajes`),
  KEY `fk_chat_orden1_idx` (`orden_idOrden`),
  KEY `fk_chat_usuarios1_idx` (`usuarios_idUsuarios`),
  CONSTRAINT `fk_chat_orden1` FOREIGN KEY (`orden_idOrden`) REFERENCES `orden` (`idOrden`),
  CONSTRAINT `fk_chat_usuarios1` FOREIGN KEY (`usuarios_idUsuarios`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `distritos`
--

DROP TABLE IF EXISTS `distritos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `distritos` (
  `idDistrito` int NOT NULL AUTO_INCREMENT,
  `nameDistrito` varchar(45) NOT NULL,
  `zonas_idZona` int NOT NULL,
  PRIMARY KEY (`idDistrito`),
  KEY `fk_distritos_zonas1_idx` (`zonas_idZona`),
  CONSTRAINT `fk_distritos_zonas1` FOREIGN KEY (`zonas_idZona`) REFERENCES `zonas` (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `distritos`
--

LOCK TABLES `distritos` WRITE;
/*!40000 ALTER TABLE `distritos` DISABLE KEYS */;
INSERT INTO `distritos` VALUES (1,'Ancón',1),(2,'Santa Rosa',1),(3,'Carabayllo',1),(4,'Puente Piedra',1),(5,'Comas',1),(6,'Los Olivos',1),(7,'San Martín de Porres',1),(8,'Independencia',1),(9,'San Juan de Miraflores',2),(10,'Villa María del Triunfo',2),(11,'Villa el Salvador',2),(12,'Pachacamac',2),(13,'Lurín',2),(14,'Punta Hermosa',2),(15,'Punta Negra',2),(16,'San Bartolo',2),(17,'Santa María del Mar',2),(18,'Pucusana',2),(19,'San Juan de Lurigancho',3),(20,'Chosica',3),(21,'Ate',3),(22,'El Agustino',3),(23,'Santa Anita',3),(24,'La Molina',3),(25,'Cieneguilla',3),(26,'Rimac',4),(27,'Cercado de Lima',4),(28,'Breña',4),(29,'Pueblo Libre',4),(30,'Magdalena',4),(31,'Jesús María',4),(32,'La Victoria',4),(33,'Lince',4),(34,'San Isidro',4),(35,'San Miguel',4),(36,'Surquillo',4),(37,'San Borja',4),(38,'Santiago de Surco',4),(39,'Barranco',4),(40,'Chorrillos',4),(41,'San Luis',4),(42,'Miraflores',4);
/*!40000 ALTER TABLE `distritos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadocodigos`
--

DROP TABLE IF EXISTS `estadocodigos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadocodigos` (
  `idEstado` int unsigned NOT NULL,
  `nombreEstado` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadocodigos`
--

LOCK TABLES `estadocodigos` WRITE;
/*!40000 ALTER TABLE `estadocodigos` DISABLE KEYS */;
INSERT INTO `estadocodigos` VALUES (1,'Habilitado'),(2,'Multado'),(3,'Cancelado'),(4,'Suspendido'),(5,'Anulado');
/*!40000 ALTER TABLE `estadocodigos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadoorden`
--

DROP TABLE IF EXISTS `estadoorden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadoorden` (
  `idEstado` int NOT NULL,
  `nombreEstado` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadoorden`
--

LOCK TABLES `estadoorden` WRITE;
/*!40000 ALTER TABLE `estadoorden` DISABLE KEYS */;
/*!40000 ALTER TABLE `estadoorden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones` (
  `idNotificaciones` int NOT NULL AUTO_INCREMENT,
  `horaCreacion` datetime NOT NULL,
  `contenido` varchar(500) NOT NULL,
  `cabecera` varchar(100) NOT NULL,
  `usuarios_idUsuarios` int NOT NULL,
  PRIMARY KEY (`idNotificaciones`),
  KEY `fk_notificaciones_usuarios1_idx` (`usuarios_idUsuarios`),
  CONSTRAINT `fk_notificaciones_usuarios1` FOREIGN KEY (`usuarios_idUsuarios`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificaciones`
--

LOCK TABLES `notificaciones` WRITE;
/*!40000 ALTER TABLE `notificaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden`
--

DROP TABLE IF EXISTS `orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden` (
  `idOrden` varchar(45) NOT NULL,
  `usuarios_idUsuarios` int NOT NULL,
  `satisfaccion_idForm` int NOT NULL,
  `estadoOrden_idEstado` int NOT NULL,
  `carrito` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`idOrden`),
  KEY `fk_orden_usuarios1_idx` (`usuarios_idUsuarios`),
  KEY `fk_orden_satisfaccion1_idx` (`satisfaccion_idForm`),
  KEY `fk_orden_estadoOrden1_idx` (`estadoOrden_idEstado`),
  CONSTRAINT `fk_orden_estadoOrden1` FOREIGN KEY (`estadoOrden_idEstado`) REFERENCES `estadoorden` (`idEstado`),
  CONSTRAINT `fk_orden_satisfaccion1` FOREIGN KEY (`satisfaccion_idForm`) REFERENCES `satisfaccion` (`idForm`),
  CONSTRAINT `fk_orden_usuarios1` FOREIGN KEY (`usuarios_idUsuarios`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden`
--

LOCK TABLES `orden` WRITE;
/*!40000 ALTER TABLE `orden` DISABLE KEYS */;
/*!40000 ALTER TABLE `orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `idProducto` int NOT NULL AUTO_INCREMENT,
  `nombreProducto` varchar(45) NOT NULL,
  `desccripcion` varchar(250) NOT NULL,
  `precio` decimal(8,2) NOT NULL,
  `costoEnvio` decimal(8,2) NOT NULL,
  `proveedores_id_proveedor` int NOT NULL,
  `paisOrigen` varchar(45) DEFAULT NULL,
  `colores` varchar(45) DEFAULT NULL,
  `talla` varchar(45) DEFAULT NULL,
  `material` varchar(45) DEFAULT NULL,
  `modelo` varchar(45) DEFAULT NULL,
  `resolucion` varchar(45) DEFAULT NULL,
  `ram` int DEFAULT NULL,
  `almacenamiento` int DEFAULT NULL,
  `categoria_idCategoria` int NOT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `fk_producto_proveedores1_idx` (`proveedores_id_proveedor`),
  KEY `fk_producto_categoria1_idx` (`categoria_idCategoria`),
  CONSTRAINT `fk_producto_categoria1` FOREIGN KEY (`categoria_idCategoria`) REFERENCES `categoria` (`idCategoria`),
  CONSTRAINT `fk_producto_proveedores1` FOREIGN KEY (`proveedores_id_proveedor`) REFERENCES `proveedores` (`id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto_porordeny_por_zona`
--

DROP TABLE IF EXISTS `producto_porordeny_por_zona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto_porordeny_por_zona` (
  `zonas_has_producto_zonas_idZona` int NOT NULL,
  `zonas_has_producto_producto_idProducto` int NOT NULL,
  `orden_idOrden` varchar(45) NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`zonas_has_producto_zonas_idZona`,`zonas_has_producto_producto_idProducto`,`orden_idOrden`),
  KEY `fk_zonas_has_producto_has_orden_orden1_idx` (`orden_idOrden`),
  KEY `fk_zonas_has_producto_has_orden_zonas_has_producto1_idx` (`zonas_has_producto_zonas_idZona`,`zonas_has_producto_producto_idProducto`),
  CONSTRAINT `fk_zonas_has_producto_has_orden_orden1` FOREIGN KEY (`orden_idOrden`) REFERENCES `orden` (`idOrden`),
  CONSTRAINT `fk_zonas_has_producto_has_orden_zonas_has_producto1` FOREIGN KEY (`zonas_has_producto_zonas_idZona`, `zonas_has_producto_producto_idProducto`) REFERENCES `zonas_has_producto` (`zonas_idZona`, `producto_idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_porordeny_por_zona`
--

LOCK TABLES `producto_porordeny_por_zona` WRITE;
/*!40000 ALTER TABLE `producto_porordeny_por_zona` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_porordeny_por_zona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `nombreProveedor` varchar(45) NOT NULL,
  `apellidoProveedor` varchar(45) NOT NULL,
  `telefonoProveedor` varchar(9) NOT NULL,
  `rucProveedor` varchar(11) NOT NULL,
  `dniProveedor` varchar(8) NOT NULL,
  `nombreTienda` varchar(45) NOT NULL,
  `enabled` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_proveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES (1,'Ernesto','Valdez','978622534','10345678329','34567832','Samsung',1),(2,'Andrés','Hurtado','956478612','10456798239','45679823','San Fernando',1),(3,'Andrés','Lujan','934678239','10452019879','45201987','Nesquick',1),(6,'Manuel','Yarlequé','958734203','10456783939','45678393','Maxwell',1),(7,'Maicol','Andrade','934218648','10654890239','65489023','Universal S.A.',1);
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `idRoles` int NOT NULL AUTO_INCREMENT,
  `nombreRol` varchar(45) NOT NULL,
  PRIMARY KEY (`idRoles`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Super Administrador'),(2,'Administrador Zonal'),(3,'Agente de Compra'),(4,'Importador');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `satisfaccion`
--

DROP TABLE IF EXISTS `satisfaccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `satisfaccion` (
  `idForm` int NOT NULL AUTO_INCREMENT,
  `foto` longblob NOT NULL,
  `calidad` tinyint NOT NULL,
  `rapidez` tinyint NOT NULL,
  `atencion` tinyint NOT NULL,
  PRIMARY KEY (`idForm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `satisfaccion`
--

LOCK TABLES `satisfaccion` WRITE;
/*!40000 ALTER TABLE `satisfaccion` DISABLE KEYS */;
/*!40000 ALTER TABLE `satisfaccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcategoria`
--

DROP TABLE IF EXISTS `subcategoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subcategoria` (
  `idsubcategoria` int NOT NULL,
  `nombreSubcategoria` varchar(45) NOT NULL,
  `categoria_idCategoria` int NOT NULL,
  PRIMARY KEY (`idsubcategoria`),
  KEY `fk_subcategoria_categoria_idx` (`categoria_idCategoria`),
  CONSTRAINT `fk_subcategoria_categoria` FOREIGN KEY (`categoria_idCategoria`) REFERENCES `categoria` (`idCategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcategoria`
--

LOCK TABLES `subcategoria` WRITE;
/*!40000 ALTER TABLE `subcategoria` DISABLE KEYS */;
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
  `fecha` timestamp NOT NULL,
  `tiempo` int NOT NULL,
  `usuarios_idUsuarios` int NOT NULL,
  PRIMARY KEY (`idToken`),
  KEY `fk_token_usuarios1_idx` (`usuarios_idUsuarios`),
  CONSTRAINT `fk_token_usuarios1` FOREIGN KEY (`usuarios_idUsuarios`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idUsuarios` int NOT NULL AUTO_INCREMENT,
  `idCoordinador` int DEFAULT NULL,
  `idAgente` int DEFAULT NULL,
  `roles_idRoles` int NOT NULL,
  `nombreUsuario` varchar(45) DEFAULT NULL,
  `apellidoUsuario` varchar(45) DEFAULT NULL,
  `dniUsuario` varchar(8) DEFAULT NULL,
  `correoUsuario` varchar(45) NOT NULL,
  `FechaNacimiento` date DEFAULT NULL,
  `contrasenaUsuario` varchar(256) NOT NULL,
  `telefonoUsuario` varchar(9) DEFAULT NULL,
  `direccionUsuario` varchar(80) DEFAULT NULL,
  `codigoDespachador` varchar(20) DEFAULT NULL,
  `rucUsuario` varchar(11) DEFAULT NULL,
  `razonSocial` varchar(45) DEFAULT NULL,
  `codigoJurisdiccion` varchar(45) DEFAULT NULL,
  `estadoCodigoDespachador_idEstado` int unsigned DEFAULT NULL,
  `zonas_idZona` int DEFAULT NULL,
  `distritos_idDistrito` int DEFAULT NULL,
  `solicitudAgente` varchar(100) DEFAULT NULL,
  `is_active` tinyint NOT NULL DEFAULT '0',
  `isAccepted` tinyint NOT NULL DEFAULT '0',
  `postulaAgente` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUsuarios`),
  KEY `fk_usuarios_roles1_idx` (`roles_idRoles`),
  KEY `fk_usuarios_distritos1_idx` (`distritos_idDistrito`),
  KEY `fk_usuarios_estadoCodigoDespachador1_idx` (`estadoCodigoDespachador_idEstado`),
  KEY `fk_usuarios_usuarios1_idx` (`idAgente`),
  KEY `fk_usuarios_usuarios2_idx` (`idCoordinador`),
  KEY `fk_usuarios_zonas1_idx` (`zonas_idZona`),
  CONSTRAINT `fk_usuarios_distritos1` FOREIGN KEY (`distritos_idDistrito`) REFERENCES `distritos` (`idDistrito`),
  CONSTRAINT `fk_usuarios_estadoCodigoDespachador1` FOREIGN KEY (`estadoCodigoDespachador_idEstado`) REFERENCES `estadocodigos` (`idEstado`),
  CONSTRAINT `fk_usuarios_roles1` FOREIGN KEY (`roles_idRoles`) REFERENCES `roles` (`idRoles`),
  CONSTRAINT `fk_usuarios_usuarios1` FOREIGN KEY (`idAgente`) REFERENCES `usuarios` (`idUsuarios`),
  CONSTRAINT `fk_usuarios_usuarios2` FOREIGN KEY (`idCoordinador`) REFERENCES `usuarios` (`idUsuarios`),
  CONSTRAINT `fk_usuarios_zonas1` FOREIGN KEY (`zonas_idZona`) REFERENCES `zonas` (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,NULL,NULL,2,'Julio','Mamani','31657803','mamani.julio@gmail.com','2002-08-13','VQ%e<!Ar*8','926789321',NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,1,1,0),(2,NULL,NULL,2,'Dina','Boluarte','23987635','dina.boluarte@gmail.com','1956-07-13','2r(412(%r^','956287401',NULL,NULL,NULL,NULL,NULL,NULL,4,NULL,NULL,0,1,0),(3,NULL,NULL,2,'Peter','Castle','37890236','peter.castle','1953-01-14',')vWXu_,2NU','998763014',NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,0,1,0),(5,NULL,NULL,2,'Chupetín','Trujillo','31902187','chuperin.trujillo@outlook.com','1952-09-13','<0w*3#Ya<|','938504612',NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,1,1,0),(6,NULL,NULL,2,'Alonso','Quijano','45983286','alonso.quijano@gmail.com','2002-08-12','kd.:R&.M7)','981275356',NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,1,1,0),(7,NULL,NULL,2,'Oscar','Carbajal','39765102','oscar.carbajal@gmail.com','2001-05-31','z?pfuBkg2+','910876395',NULL,NULL,NULL,NULL,NULL,NULL,2,NULL,NULL,0,1,0),(16,NULL,NULL,4,'José','Peña','31679102','jose.peña@gmail.com',NULL,'m2s.sfju+-','901678294','Av. Margaritas','A092-2239','10316791029','Pepsico Iberia Servicios Centrales, S.L.','A00929301',1,1,6,'1',1,1,1),(17,NULL,NULL,3,'Andrea','Caro','49028736','andrea.caro@gmail.com',NULL,'suuu19.2+','917020891','Av. Tulipanes','B045-8391','10490287369','Ayudín S.A.','B00492124',1,1,4,'0',1,1,0),(18,NULL,NULL,4,'Samir','Vela','30921655','samir.vela@gmail.com',NULL,'sj-d.-12435','920100493','Av. Claveles','C923-1234','10309216559','Hyundai S.A.','C12012854',1,2,15,'1',1,1,1),(19,NULL,NULL,3,'Raul','Donayre','52918472','raul.donayre@gmail.com',NULL,'slxkc.k.ue.s','910018265','Av. Rosas','D172-1245','10529184729','Huawei S.A.','D12442892',1,2,12,'0',0,1,0),(20,NULL,NULL,4,'Antony','Aparicio','51029842','antony.aparicio@gmail.com',NULL,'as.sdg/sfds',NULL,'Av. Girasoles',NULL,NULL,NULL,NULL,NULL,NULL,2,'0',1,1,0),(21,NULL,NULL,4,'Armando','Jupanqui','42018347','armando.yupanqui@gmail.com',NULL,'sdsan4.ren',NULL,'Av. Magdalenas',NULL,NULL,NULL,NULL,NULL,NULL,10,'0',0,0,0),(22,NULL,NULL,4,'Romel','Soto','30197562','romel.soto@gmail.com',NULL,'s()82jfn.sa',NULL,'Av. Hortencias',NULL,NULL,NULL,NULL,NULL,NULL,15,'0',0,1,0),(23,NULL,NULL,4,'Pamela','Franco','41829439','pamela.franco@gmail.com',NULL,'ifd.w8238+s',NULL,'Av. Capulís',NULL,NULL,NULL,NULL,NULL,NULL,40,'0',0,0,0);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zonas`
--

DROP TABLE IF EXISTS `zonas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zonas` (
  `idZona` int NOT NULL AUTO_INCREMENT,
  `nameZona` varchar(10) NOT NULL,
  PRIMARY KEY (`idZona`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zonas`
--

LOCK TABLES `zonas` WRITE;
/*!40000 ALTER TABLE `zonas` DISABLE KEYS */;
INSERT INTO `zonas` VALUES (1,'Norte'),(2,'Sur'),(3,'Este'),(4,'Oeste');
/*!40000 ALTER TABLE `zonas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zonas_has_producto`
--

DROP TABLE IF EXISTS `zonas_has_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zonas_has_producto` (
  `zonas_idZona` int NOT NULL,
  `producto_idProducto` int NOT NULL,
  `cantidad` int NOT NULL,
  `arribo` date DEFAULT NULL,
  `bajoStock` tinyint NOT NULL,
  PRIMARY KEY (`zonas_idZona`,`producto_idProducto`),
  KEY `fk_zonas_has_producto_producto1_idx` (`producto_idProducto`),
  KEY `fk_zonas_has_producto_zonas1_idx` (`zonas_idZona`),
  CONSTRAINT `fk_zonas_has_producto_producto1` FOREIGN KEY (`producto_idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `fk_zonas_has_producto_zonas1` FOREIGN KEY (`zonas_idZona`) REFERENCES `zonas` (`idZona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zonas_has_producto`
--

LOCK TABLES `zonas_has_producto` WRITE;
/*!40000 ALTER TABLE `zonas_has_producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `zonas_has_producto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-25 13:35:52
