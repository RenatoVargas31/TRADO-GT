-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema trado_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema trado_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trado_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `trado_db` ;

-- -----------------------------------------------------
-- Table `trado_db`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`estadoordenagente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`estadoordenagente` (
  `idEstadoOrdenAgente` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstadoOrdenAgente`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`estadoordenimportador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`estadoordenimportador` (
  `idEstadoOrdenImportador` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idEstadoOrdenImportador`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`zona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`zona` (
  `idZona` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idZona`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`distrito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`distrito` (
  `idDistrito` INT NOT NULL AUTO_INCREMENT,
  `Zona_idZona` INT NOT NULL,
  `Codigo` VARCHAR(45) NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDistrito`),
  INDEX `fk_Distrito_Zona1_idx` (`Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_Distrito_Zona1`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `trado_db`.`zona` (`idZona`))
ENGINE = InnoDB
AUTO_INCREMENT = 43
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRol`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`estadocodigo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`estadocodigo` (
  `idEstadoCodigo` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstadoCodigo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`codigodespachador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`codigodespachador` (
  `idCodigoDespachador` INT NOT NULL AUTO_INCREMENT,
  `Caracteres` VARCHAR(45) NOT NULL,
  `EstadoCodigo_idEstadoCodigo` INT NOT NULL,
  `zona_idZona` INT NOT NULL,
  PRIMARY KEY (`idCodigoDespachador`),
  INDEX `fk_CodigoDespachador_EstadoCodigo1_idx` (`EstadoCodigo_idEstadoCodigo` ASC) VISIBLE,
  INDEX `fk_codigodespachador_zona1_idx` (`zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_CodigoDespachador_EstadoCodigo1`
    FOREIGN KEY (`EstadoCodigo_idEstadoCodigo`)
    REFERENCES `trado_db`.`estadocodigo` (`idEstadoCodigo`),
  CONSTRAINT `fk_codigodespachador_zona1`
    FOREIGN KEY (`zona_idZona`)
    REFERENCES `trado_db`.`zona` (`idZona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `Rol_idRol` INT NOT NULL,
  `AdmZonal_idUsuario` INT NULL DEFAULT NULL,
  `AgentCompra_idUsuario` INT NULL DEFAULT NULL,
  `Zona_idZona` INT NULL DEFAULT NULL,
  `Distrito_idDistrito` INT NULL DEFAULT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido` VARCHAR(45) NOT NULL,
  `FechaNacimiento` DATE NULL DEFAULT NULL,
  `Dni` VARCHAR(8) NULL DEFAULT NULL,
  `Telefono` VARCHAR(9) NULL DEFAULT NULL,
  `Correo` VARCHAR(45) NULL DEFAULT NULL,
  `Contrasena` VARCHAR(45) NULL DEFAULT NULL,
  `Ruc` VARCHAR(45) NULL DEFAULT NULL,
  `RazonSocial` VARCHAR(45) NULL DEFAULT NULL,
  `Direccion` VARCHAR(45) NULL DEFAULT NULL,
  `isAccepted` TINYINT NULL DEFAULT '0',
  `isPostulated` TINYINT NULL DEFAULT '0',
  `isActivated` TINYINT NULL DEFAULT '0',
  `Foto` VARCHAR(45) NULL DEFAULT NULL,
  `MotivoBanneo` VARCHAR(45) NULL DEFAULT NULL,
  `FechaBanneo` TIMESTAMP NULL DEFAULT NULL,
  `codigodespachador_idCodigoDespachador` INT NULL DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX `fk_Usuario_Rol_idx` (`Rol_idRol` ASC) VISIBLE,
  INDEX `fk_Usuario_Zona1_idx` (`Zona_idZona` ASC) VISIBLE,
  INDEX `fk_Usuario_Distrito1_idx` (`Distrito_idDistrito` ASC) VISIBLE,
  INDEX `fk_Usuario_Usuario1_idx` (`AdmZonal_idUsuario` ASC) VISIBLE,
  INDEX `fk_Usuario_Usuario2_idx` (`AgentCompra_idUsuario` ASC) VISIBLE,
  INDEX `fk_usuario_codigodespachador1_idx` (`codigodespachador_idCodigoDespachador` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_Distrito1`
    FOREIGN KEY (`Distrito_idDistrito`)
    REFERENCES `trado_db`.`distrito` (`idDistrito`),
  CONSTRAINT `fk_Usuario_Rol`
    FOREIGN KEY (`Rol_idRol`)
    REFERENCES `trado_db`.`rol` (`idRol`),
  CONSTRAINT `fk_Usuario_Usuario1`
    FOREIGN KEY (`AdmZonal_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`),
  CONSTRAINT `fk_Usuario_Usuario2`
    FOREIGN KEY (`AgentCompra_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`),
  CONSTRAINT `fk_Usuario_Zona1`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `trado_db`.`zona` (`idZona`),
  CONSTRAINT `fk_usuario_codigodespachador1`
    FOREIGN KEY (`codigodespachador_idCodigoDespachador`)
    REFERENCES `trado_db`.`codigodespachador` (`idCodigoDespachador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`valoracion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`valoracion` (
  `idValoracion` INT NOT NULL AUTO_INCREMENT,
  `Valor` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idValoracion`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`orden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`orden` (
  `idOrden` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `AgentCompra_idUsuario` INT NULL DEFAULT NULL,
  `Valoracion_idValoracion` INT NULL DEFAULT NULL,
  `EstadoOrdenAgente_idEstadoOrdenAgente` INT NULL DEFAULT NULL,
  `EstadoOrdenImportador_idEstadoOrdenImportador` INT NULL DEFAULT NULL,
  `FechaArribo` TIMESTAMP NOT NULL,
  `FechaCreacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `isDeleted` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`idOrden`),
  INDEX `fk_Orden_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Orden_Usuario2_idx` (`AgentCompra_idUsuario` ASC) VISIBLE,
  INDEX `fk_Orden_Valoracion1_idx` (`Valoracion_idValoracion` ASC) VISIBLE,
  INDEX `fk_Orden_EstadoOrdenAgente1_idx` (`EstadoOrdenAgente_idEstadoOrdenAgente` ASC) VISIBLE,
  INDEX `fk_Orden_EstadoOrdenImportador1_idx` (`EstadoOrdenImportador_idEstadoOrdenImportador` ASC) VISIBLE,
  CONSTRAINT `fk_Orden_EstadoOrdenAgente1`
    FOREIGN KEY (`EstadoOrdenAgente_idEstadoOrdenAgente`)
    REFERENCES `trado_db`.`estadoordenagente` (`idEstadoOrdenAgente`),
  CONSTRAINT `fk_Orden_EstadoOrdenImportador1`
    FOREIGN KEY (`EstadoOrdenImportador_idEstadoOrdenImportador`)
    REFERENCES `trado_db`.`estadoordenimportador` (`idEstadoOrdenImportador`),
  CONSTRAINT `fk_Orden_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`),
  CONSTRAINT `fk_Orden_Usuario2`
    FOREIGN KEY (`AgentCompra_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`),
  CONSTRAINT `fk_Orden_Valoracion1`
    FOREIGN KEY (`Valoracion_idValoracion`)
    REFERENCES `trado_db`.`valoracion` (`idValoracion`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`chat` (
  `idChat` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Orden_idOrden` INT NOT NULL,
  `Mensaje` VARCHAR(45) NOT NULL,
  `Tiempo` INT NOT NULL,
  PRIMARY KEY (`idChat`),
  INDEX `fk_Chat_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Chat_Orden1_idx` (`Orden_idOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Chat_Orden1`
    FOREIGN KEY (`Orden_idOrden`)
    REFERENCES `trado_db`.`orden` (`idOrden`),
  CONSTRAINT `fk_Chat_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`chatbot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`chatbot` (
  `idChatBot` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Mensaje` VARCHAR(45) NOT NULL,
  `Respuesta` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idChatBot`),
  INDEX `fk_ChatBot_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_ChatBot_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`notificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`notificacion` (
  `idNotificacion` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Contenido` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idNotificacion`),
  INDEX `fk_Notificacion_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Notificacion_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`pago` (
  `idPago` INT NOT NULL AUTO_INCREMENT,
  `Orden_idOrden` INT NOT NULL,
  `Metodo` VARCHAR(45) NOT NULL,
  `Monto` DECIMAL(10,2) NOT NULL,
  `Fecha` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idPago`),
  INDEX `fk_Pago_Orden1_idx` (`Orden_idOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Pago_Orden1`
    FOREIGN KEY (`Orden_idOrden`)
    REFERENCES `trado_db`.`orden` (`idOrden`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`pregunta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`pregunta` (
  `idPregunta` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Contenido` VARCHAR(80) NOT NULL,
  `Fecha` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idPregunta`),
  INDEX `fk_Consulta_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Consulta_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`proveedor` (
  `idProveedor` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Telefono` VARCHAR(9) NOT NULL,
  `Ruc` VARCHAR(45) NOT NULL,
  `Dni` VARCHAR(8) NOT NULL,
  `Tienda` VARCHAR(45) NOT NULL,
  `isDeleted` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`idProveedor`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`subcategoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`subcategoria` (
  `idSubCategoria` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Categoria_idCategoria` INT NOT NULL,
  PRIMARY KEY (`idSubCategoria`),
  INDEX `fk_SubCategoria_Categoria1_idx` (`Categoria_idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_SubCategoria_Categoria1`
    FOREIGN KEY (`Categoria_idCategoria`)
    REFERENCES `trado_db`.`categoria` (`idCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`producto` (
  `idProducto` INT NOT NULL AUTO_INCREMENT,
  `Proveedor_idProveedor` INT NOT NULL,
  `SubCategoria_idSubCategoria` INT NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Cantidad` VARCHAR(45) NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Precio` VARCHAR(45) NOT NULL,
  `Color` VARCHAR(45) NULL DEFAULT NULL,
  `Talla` VARCHAR(45) NULL DEFAULT NULL,
  `Material` VARCHAR(45) NULL DEFAULT NULL,
  `Modelo` VARCHAR(45) NULL DEFAULT NULL,
  `Resolucion` VARCHAR(45) NULL DEFAULT NULL,
  `Ram` VARCHAR(45) NULL DEFAULT NULL,
  `Almacenamiento` VARCHAR(45) NULL DEFAULT NULL,
  `isDeleted` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`idProducto`),
  INDEX `fk_Producto_Proveedor1_idx` (`Proveedor_idProveedor` ASC) VISIBLE,
  INDEX `fk_Producto_SubCategoria1_idx` (`SubCategoria_idSubCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_Proveedor1`
    FOREIGN KEY (`Proveedor_idProveedor`)
    REFERENCES `trado_db`.`proveedor` (`idProveedor`),
  CONSTRAINT `fk_Producto_SubCategoria1`
    FOREIGN KEY (`SubCategoria_idSubCategoria`)
    REFERENCES `trado_db`.`subcategoria` (`idSubCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`productoenzona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`productoenzona` (
  `Producto_idProducto` INT NOT NULL,
  `Zona_idZona` INT NOT NULL,
  `Cantidad` INT NOT NULL,
  `CostoEnvio` DECIMAL(10,2) NOT NULL,
  `estadoRepo` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`Producto_idProducto`, `Zona_idZona`),
  INDEX `fk_Producto_has_Zona_Zona1_idx` (`Zona_idZona` ASC) VISIBLE,
  INDEX `fk_Producto_has_Zona_Producto1_idx` (`Producto_idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_has_Zona_Producto1`
    FOREIGN KEY (`Producto_idProducto`)
    REFERENCES `trado_db`.`producto` (`idProducto`),
  CONSTRAINT `fk_Producto_has_Zona_Zona1`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `trado_db`.`zona` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`productoenzonaenorden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`productoenzonaenorden` (
  `ProductoEnZona_Producto_idProducto` INT NOT NULL,
  `ProductoEnZona_Zona_idZona` INT NOT NULL,
  `Orden_idOrden` INT NOT NULL,
  `Cantidad` INT NOT NULL,
  PRIMARY KEY (`ProductoEnZona_Producto_idProducto`, `ProductoEnZona_Zona_idZona`, `Orden_idOrden`),
  INDEX `fk_ProductoEnZona_has_Orden_Orden1_idx` (`Orden_idOrden` ASC) VISIBLE,
  INDEX `fk_ProductoEnZona_has_Orden_ProductoEnZona1_idx` (`ProductoEnZona_Producto_idProducto` ASC, `ProductoEnZona_Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_ProductoEnZona_has_Orden_Orden1`
    FOREIGN KEY (`Orden_idOrden`)
    REFERENCES `trado_db`.`orden` (`idOrden`),
  CONSTRAINT `fk_ProductoEnZona_has_Orden_ProductoEnZona1`
    FOREIGN KEY (`ProductoEnZona_Producto_idProducto` , `ProductoEnZona_Zona_idZona`)
    REFERENCES `trado_db`.`productoenzona` (`Producto_idProducto` , `Zona_idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`resena`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`resena` (
  `idResena` INT NOT NULL AUTO_INCREMENT,
  `Foto` VARCHAR(45) NOT NULL,
  `Comentario` VARCHAR(200) NOT NULL,
  `Calidad` VARCHAR(45) NOT NULL,
  `TiempoEntrega` VARCHAR(45) NOT NULL,
  `Producto_idProducto` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idResena`),
  INDEX `fk_Resena_Producto1_idx` (`Producto_idProducto` ASC) VISIBLE,
  INDEX `fk_Resena_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Resena_Producto1`
    FOREIGN KEY (`Producto_idProducto`)
    REFERENCES `trado_db`.`producto` (`idProducto`),
  CONSTRAINT `fk_Resena_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`token` (
  `idToken` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Tipo` VARCHAR(45) NOT NULL,
  `Duracion` INT NOT NULL,
  `FechaCreacion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idToken`),
  INDEX `fk_Token_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Token_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trado_db`.`usuariorespuesta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trado_db`.`usuariorespuesta` (
  `Pregunta_idPregunta` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  `Contenido` VARCHAR(100) NOT NULL,
  `Fecha` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Pregunta_idPregunta`, `Usuario_idUsuario`),
  INDEX `fk_Pregunta_has_Usuario_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Pregunta_has_Usuario_Pregunta1_idx` (`Pregunta_idPregunta` ASC) VISIBLE,
  CONSTRAINT `fk_Pregunta_has_Usuario_Pregunta1`
    FOREIGN KEY (`Pregunta_idPregunta`)
    REFERENCES `trado_db`.`pregunta` (`idPregunta`),
  CONSTRAINT `fk_Pregunta_has_Usuario_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `trado_db`.`usuario` (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
