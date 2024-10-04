-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema stable-TRADO_DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema stable-TRADO_DB
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `stable-TRADO_DB`;
CREATE SCHEMA IF NOT EXISTS `stable-TRADO_DB` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `stable-TRADO_DB` ;

-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `Categoria` DISABLE KEYS */;
INSERT INTO `Categoria` VALUES (1,'Ropa mujer'),(2,'Ropa hombre'),(3,'Tecnología'), (4,'Muebles');
/*!40000 ALTER TABLE `Categoria` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`EstadoOrden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`EstadoOrden` (
  `idEstadoOrden` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstadoOrden`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `EstadoOrden` DISABLE KEYS */;
INSERT INTO `EstadoOrden` VALUES (1,'Creado'),(2,'En Validación'),(3,'En proceso'),(4,'Arribo al País'),(5,'En aduanas'),(6,'En Ruta'),(7,'Recibido');
/*!40000 ALTER TABLE `EstadoOrden` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`EstadoCodigoD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`EstadoCodigoD` (
  `idEstadoCodigo` INT NOT NULL,
  `estado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstadoCodigo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `EstadoCodigoD` DISABLE KEYS */;
INSERT INTO `EstadoCodigoD` VALUES (1,'Habilitado'),(2,'Multado'),(3,'Cancelado'),(4,'Suspendido'),(5,'Anulado en jurisdicción');
/*!40000 ALTER TABLE `EstadoCodigoD` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`CodigoDespachador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`CodigoDespachador` (
  `idCodigoDespachador` INT NOT NULL,
  `codigo` INT NOT NULL,
  `estadoCodigo_idEstadoCodigo` INT NOT NULL,
  PRIMARY KEY (`idCodigoDespachador`),
  INDEX `fk_CodigoDespachador_EstadoCodigo1_idx` (`estadoCodigo_idEstadoCodigo` ASC) VISIBLE,
  CONSTRAINT `fk_CodigoDespachador_EstadoCodigo1`
    FOREIGN KEY (`estadoCodigo_idEstadoCodigo`)
    REFERENCES `stable-TRADO_DB`.`EstadoCodigoD` (`idEstadoCodigo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`CodigoJurisdiccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`CodigoJurisdiccion` (
  `idCodigoJurisdiccion` INT NOT NULL,
  `codigo` INT NOT NULL,
  PRIMARY KEY (`idCodigoJurisdiccion`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Zona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Zona` (
  `idZona` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `Zona` DISABLE KEYS */;
INSERT INTO `Zona` VALUES (1,'Norte'),(2,'Sur'),(3,'Este'),(4,'Oeste');
/*!40000 ALTER TABLE `Zona` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Distrito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Distrito` (
  `idDistrito` INT NOT NULL AUTO_INCREMENT,
  `zona_idZona` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDistrito`),
  INDEX `fk_Distrito_Zona1_idx` (`zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_Distrito_Zona1`
    FOREIGN KEY (`zona_idZona`)
    REFERENCES `stable-TRADO_DB`.`Zona` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `Distrito` DISABLE KEYS */;
INSERT INTO `Distrito` VALUES (1,1,'Ancon'),(2,1,'Santa Rosa'),(3,1,'Carabayllo'),(4,1,'Puente Piedra'),(5,1,'Comas'),(6,1,'Los Olivos'),(7,1,'San Martín de Porres'),(8,1,'Independencia'),(9,2,'San Juan de Miraflores'),(10,2,'Villa María del Triunfo'),(11,2,'Villa el Salvador'),(12,2,'Pachacamac'),(13,2,'Lurin'),(14,2,'Punta Hermosa'),(15,2,'Punta Negra'),(16,2,'San Bartolo'),(17,2,'Santa María del Mar'),(18,2,'Pucusana'),(19,3,'San Juan de Lurigancho'),(20,3,'Lurigancho/Chosica'),(21,3,'Ate'),(22,3,'El Agustino'),(23,3,'Santa Anita'),(24,3,'La Molina'),(25,3,'Cieneguilla'),(26,4,'Rimac'),(27,4,'Cercado de Lima'),(28,4,'Breña'),(29,4,'Pueblo Libre'),(30,4,'Magdalena'),(31,4,'Jesus María'),(32,4,'La Victoria'),(33,4,'Lince'),(34,4,'San Isidro'),(35,4,'San Miguel'),(36,4,'Surquillo'),(37,4,'San Borja'),(38,4,'Santiago de Surco'),(39,4,'Barranco'),(40,4,'Chorrillos'),(41,4,'San Luis'),(42,4,'Miraflores');
/*!40000 ALTER TABLE `Distrito` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRol`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `Rol` DISABLE KEYS */;
INSERT INTO `Rol` VALUES (1,'SuperAdmin'),(2,'Administrador Zonal'),(3,'Agente de Compra'),(4,'Usuario Final');
/*!40000 ALTER TABLE `Rol` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `rol_idRol` INT NOT NULL,
  `admZonal_idUsuario` INT NULL DEFAULT NULL,
  `agentCompra_idUsuario` INT NULL DEFAULT NULL,
  `zona_idZona` INT NULL DEFAULT NULL,
  `distrito_idDistrito` INT NULL DEFAULT NULL,
  `foto` VARCHAR(45) NULL DEFAULT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `fechaNacimiento` DATE NULL DEFAULT NULL,
  `dni` VARCHAR(8) NULL DEFAULT NULL,
  `telefono` VARCHAR(9) NULL DEFAULT NULL,
  `correo` VARCHAR(45) NULL DEFAULT NULL,
  `contrasena` VARCHAR(45) NULL DEFAULT NULL,
  `ruc` VARCHAR(10) NULL DEFAULT NULL,
  `razonSocial` VARCHAR(45) NULL DEFAULT NULL,
  `direccion` VARCHAR(45) NULL DEFAULT NULL,
  `isAccepted` TINYINT NOT NULL DEFAULT '0',
  `isPostulated` TINYINT NULL DEFAULT '0',
  `isActivated` TINYINT NOT NULL DEFAULT '0',
  `codigoDespachador_idCodigoDespachador` INT NULL DEFAULT NULL,
  `codigoJurisdiccion_idCodigoJurisdiccion` INT NULL DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX `fk_Usuario_Rol_idx` (`rol_idRol` ASC) VISIBLE,
  INDEX `fk_Usuario_Zona1_idx` (`zona_idZona` ASC) VISIBLE,
  INDEX `fk_Usuario_Distrito1_idx` (`distrito_idDistrito` ASC) VISIBLE,
  INDEX `fk_Usuario_Usuario1_idx` (`admZonal_idUsuario` ASC) VISIBLE,
  INDEX `fk_Usuario_Usuario2_idx` (`agentCompra_idUsuario` ASC) VISIBLE,
  INDEX `fk_Usuario_CodigoDespachador1_idx` (`codigoDespachador_idCodigoDespachador` ASC) VISIBLE,
  INDEX `fk_Usuario_CodigoJurisdiccion1_idx` (`codigoJurisdiccion_idCodigoJurisdiccion` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_CodigoDespachador1`
    FOREIGN KEY (`codigoDespachador_idCodigoDespachador`)
    REFERENCES `stable-TRADO_DB`.`CodigoDespachador` (`idCodigoDespachador`),
  CONSTRAINT `fk_Usuario_CodigoJurisdiccion1`
    FOREIGN KEY (`codigoJurisdiccion_idCodigoJurisdiccion`)
    REFERENCES `stable-TRADO_DB`.`CodigoJurisdiccion` (`idCodigoJurisdiccion`),
  CONSTRAINT `fk_Usuario_Distrito1`
    FOREIGN KEY (`distrito_idDistrito`)
    REFERENCES `stable-TRADO_DB`.`Distrito` (`idDistrito`),
  CONSTRAINT `fk_Usuario_Rol`
    FOREIGN KEY (`rol_idRol`)
    REFERENCES `stable-TRADO_DB`.`Rol` (`idRol`),
  CONSTRAINT `fk_Usuario_Usuario1`
    FOREIGN KEY (`admZonal_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`),
  CONSTRAINT `fk_Usuario_Usuario2`
    FOREIGN KEY (`agentCompra_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`),
  CONSTRAINT `fk_Usuario_Zona1`
    FOREIGN KEY (`zona_idZona`)
    REFERENCES `stable-TRADO_DB`.`Zona` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Orden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Orden` (
  `idOrden` INT NOT NULL,
  `usuario_idUsuario` INT NOT NULL,
  `agentCompra_idUsuario` INT NULL DEFAULT NULL,
  `estadoOrden_idEstadoOrden` INT NULL DEFAULT NULL,
  `fechaCreacion` TIMESTAMP NULL DEFAULT NULL,
  `fechaArribo` DATE NULL DEFAULT NULL,
  `valoracionAgente` TINYINT NULL DEFAULT NULL,
  `isDeleted` TINYINT NULL DEFAULT '0',
  `fueRapido` TINYINT NULL DEFAULT NULL,
  `esCarrito` TINYINT NOT NULL DEFAULT '1',
  PRIMARY KEY (`idOrden`),
  INDEX `fk_Orden_Usuario1_idx` (`usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Orden_Usuario2_idx` (`agentCompra_idUsuario` ASC) VISIBLE,
  INDEX `fk_Orden_EstadoOrden1_idx` (`estadoOrden_idEstadoOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Orden_EstadoOrden1`
    FOREIGN KEY (`estadoOrden_idEstadoOrden`)
    REFERENCES `stable-TRADO_DB`.`EstadoOrden` (`idEstadoOrden`),
  CONSTRAINT `fk_Orden_Usuario1`
    FOREIGN KEY (`usuario_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`),
  CONSTRAINT `fk_Orden_Usuario2`
    FOREIGN KEY (`agentCompra_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Chat` (
  `idChat` INT NOT NULL,
  `usuario_idUsuario` INT NOT NULL,
  `orden_idOrden` INT NOT NULL,
  `mensaje` VARCHAR(45) NOT NULL,
  `tiempo` INT NOT NULL,
  PRIMARY KEY (`idChat`),
  INDEX `fk_Chat_Usuario1_idx` (`usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Chat_Orden1_idx` (`orden_idOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Chat_Orden1`
    FOREIGN KEY (`orden_idOrden`)
    REFERENCES `stable-TRADO_DB`.`Orden` (`idOrden`),
  CONSTRAINT `fk_Chat_Usuario1`
    FOREIGN KEY (`usuario_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Notificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Notificacion` (
  `idNotificacion` INT NOT NULL,
  `usuario_idUsuario` INT NOT NULL,
  `contenido` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idNotificacion`),
  INDEX `fk_Notificacion_Usuario1_idx` (`usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Notificacion_Usuario1`
    FOREIGN KEY (`usuario_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Pago` (
  `idPago` INT NOT NULL,
  `orden_idOrden` INT NOT NULL,
  `metodo` VARCHAR(45) NOT NULL,
  `monto` DECIMAL(10,2) NOT NULL,
  `fecha` DATE NOT NULL,
  PRIMARY KEY (`idPago`),
  INDEX `fk_Pago_Orden1_idx` (`orden_idOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Pago_Orden1`
    FOREIGN KEY (`orden_idOrden`)
    REFERENCES `stable-TRADO_DB`.`Orden` (`idOrden`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Pregunta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Pregunta` (
  `idPregunta` INT NOT NULL,
  `usuario_idUsuario` INT NOT NULL,
  `contenido` VARCHAR(80) NOT NULL,
  `fecha` DATETIME NOT NULL,
  PRIMARY KEY (`idPregunta`),
  INDEX `fk_Consulta_Usuario1_idx` (`usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Consulta_Usuario1`
    FOREIGN KEY (`usuario_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Proveedor` (
  `idProveedor` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(9) NOT NULL,
  `ruc` VARCHAR(45) NOT NULL,
  `dni` VARCHAR(8) NOT NULL,
  `tienda` VARCHAR(45) NOT NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`idProveedor`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`SubCategoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`SubCategoria` (
  `idSubCategoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `Categoria_idCategoria` INT NOT NULL,
  PRIMARY KEY (`idSubCategoria`),
  INDEX `fk_SubCategoria_Categoria1_idx` (`Categoria_idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_SubCategoria_Categoria1`
    FOREIGN KEY (`Categoria_idCategoria`)
    REFERENCES `stable-TRADO_DB`.`Categoria` (`idCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `SubCategoria` DISABLE KEYS */;
INSERT INTO `SubCategoria` VALUES (1,'Blusas',1),(2,'Short',1),(3,'Poleras',1),(4,'Pantalones',1),(5,'Vestidos',1),
(6,'Polos',2),(7,'Poleras',2),(8,'Camisas',2),(9,'Chompas',2),(10,'Pantalones',2),
(11,'Computadoras',3),(12,'Laptops',3),(13,'Tablets',3),(14,'Celulares',3),(15,'Auriculares',3),
(16,'Juego de comedor',4),(17,'Sillones',4),(18,'Armarios',4),(19,'Literas',4),(20,'Estantes',4);
/*!40000 ALTER TABLE `SubCategoria` ENABLE KEYS */;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Producto` (
  `idProducto` INT NOT NULL AUTO_INCREMENT,
  `proveedor_idProveedor` INT NOT NULL,
  `subCategoria_idSubCategoria` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(250) NOT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `color` VARCHAR(45) NOT NULL,
  `talla` VARCHAR(45) NULL DEFAULT NULL,
  `material` VARCHAR(45) NULL DEFAULT NULL,
  `modelo` VARCHAR(45) NULL DEFAULT NULL,
  `resolucion` VARCHAR(45) NULL DEFAULT NULL,
  `ram` VARCHAR(45) NULL DEFAULT NULL,
  `almacenamiento` VARCHAR(45) NULL DEFAULT NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT '0',
  `peso` DECIMAL(10,2) NOT NULL,
  `marca` VARCHAR(45) NOT NULL,
  `ancho` VARCHAR(45) NOT NULL,
  `alto` VARCHAR(45) NOT NULL,
  `profundidad` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idProducto`),
  INDEX `fk_Producto_Proveedor1_idx` (`proveedor_idProveedor` ASC) VISIBLE,
  INDEX `fk_Producto_SubCategoria1_idx` (`subCategoria_idSubCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_Proveedor1`
    FOREIGN KEY (`proveedor_idProveedor`)
    REFERENCES `stable-TRADO_DB`.`Proveedor` (`idProveedor`),
  CONSTRAINT `fk_Producto_SubCategoria1`
    FOREIGN KEY (`subCategoria_idSubCategoria`)
    REFERENCES `stable-TRADO_DB`.`SubCategoria` (`idSubCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`ProductoEnZona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`ProductoEnZona` (
  `producto_idProducto` INT NOT NULL,
  `zona_idZona` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `costoEnvio` DECIMAL(10,2) NOT NULL,
  `estadoRepo` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`producto_idProducto`, `zona_idZona`),
  INDEX `fk_Producto_has_Zona_Zona1_idx` (`zona_idZona` ASC) VISIBLE,
  INDEX `fk_Producto_has_Zona_Producto1_idx` (`producto_idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_has_Zona_Producto1`
    FOREIGN KEY (`producto_idProducto`)
    REFERENCES `stable-TRADO_DB`.`Producto` (`idProducto`),
  CONSTRAINT `fk_Producto_has_Zona_Zona1`
    FOREIGN KEY (`zona_idZona`)
    REFERENCES `stable-TRADO_DB`.`Zona` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`ProductoEnZonaEnOrden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`ProductoEnZonaEnOrden` (
  `productoEnZona_Producto_idProducto` INT NOT NULL,
  `productoEnZona_Zona_idZona` INT NOT NULL,
  `orden_idOrden` INT NOT NULL,
  `cantidad` INT NOT NULL,
  PRIMARY KEY (`productoEnZona_Producto_idProducto`, `productoEnZona_Zona_idZona`, `orden_idOrden`),
  INDEX `fk_ProductoEnZona_has_Orden_Orden1_idx` (`orden_idOrden` ASC) VISIBLE,
  INDEX `fk_ProductoEnZona_has_Orden_ProductoEnZona1_idx` (`productoEnZona_Producto_idProducto` ASC, `productoEnZona_Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_ProductoEnZona_has_Orden_Orden1`
    FOREIGN KEY (`orden_idOrden`)
    REFERENCES `stable-TRADO_DB`.`Orden` (`idOrden`),
  CONSTRAINT `fk_ProductoEnZona_has_Orden_ProductoEnZona1`
    FOREIGN KEY (`productoEnZona_Producto_idProducto` , `productoEnZona_Zona_idZona`)
    REFERENCES `stable-TRADO_DB`.`ProductoEnZona` (`producto_idProducto` , `zona_idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Resena`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Resena` (
  `idResena` INT NOT NULL,
  `Producto_idProducto` INT NOT NULL,
  `foto` VARCHAR(100) NOT NULL,
  `comentario` VARCHAR(250) NOT NULL,
  `calidad` TINYINT NOT NULL,
  `calificacion` TINYINT NOT NULL,
  PRIMARY KEY (`idResena`),
  INDEX `fk_Resena_Producto1_idx` (`Producto_idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_Resena_Producto1`
    FOREIGN KEY (`Producto_idProducto`)
    REFERENCES `stable-TRADO_DB`.`Producto` (`idProducto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`Token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`Token` (
  `idToken` INT NOT NULL AUTO_INCREMENT,
  `usuario_idUsuario` INT NOT NULL,
  `duracion` INT NOT NULL,
  `fechaCreacion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idToken`),
  INDEX `fk_Token_Usuario1_idx` (`usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Token_Usuario1`
    FOREIGN KEY (`usuario_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `stable-TRADO_DB`.`UsuarioRespuesta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `stable-TRADO_DB`.`UsuarioRespuesta` (
  `pregunta_idPregunta` INT NOT NULL,
  `usuario_idUsuario` INT NOT NULL,
  `contenido` VARCHAR(100) NOT NULL,
  `fecha` DATETIME NOT NULL,
  PRIMARY KEY (`pregunta_idPregunta`, `usuario_idUsuario`),
  INDEX `fk_Pregunta_has_Usuario_Usuario1_idx` (`usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Pregunta_has_Usuario_Pregunta1_idx` (`pregunta_idPregunta` ASC) VISIBLE,
  CONSTRAINT `fk_Pregunta_has_Usuario_Pregunta1`
    FOREIGN KEY (`pregunta_idPregunta`)
    REFERENCES `stable-TRADO_DB`.`Pregunta` (`idPregunta`),
  CONSTRAINT `fk_Pregunta_has_Usuario_Usuario1`
    FOREIGN KEY (`usuario_idUsuario`)
    REFERENCES `stable-TRADO_DB`.`Usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
