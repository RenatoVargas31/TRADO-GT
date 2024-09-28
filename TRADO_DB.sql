-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema TRADO_DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema TRADO_DB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `TRADO_DB` ;
USE `TRADO_DB` ;

-- -----------------------------------------------------
-- Table `TRADO_DB`.`Rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRol`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Zona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Zona` (
  `idZona` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idZona`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Distrito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Distrito` (
  `idDistrito` INT NOT NULL AUTO_INCREMENT,
  `Zona_idZona` INT NOT NULL,
  `Codigo` VARCHAR(45) NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDistrito`),
  INDEX `fk_Distrito_Zona1_idx` (`Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_Distrito_Zona1`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `TRADO_DB`.`Zona` (`idZona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `Rol_idRol` INT NOT NULL,
  `AdmZonal_idUsuario` INT NULL,
  `AgentCompra_idUsuario` INT NULL,
  `Zona_idZona` INT NULL,
  `Distrito_idDistrito` INT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido` VARCHAR(45) NOT NULL,
  `FechaNacimiento` DATE NULL,
  `Dni` VARCHAR(8) NULL,
  `Telefono` VARCHAR(9) NULL,
  `Correo` VARCHAR(45) NULL,
  `Contrasena` VARCHAR(45) NULL,
  `Ruc` VARCHAR(45) NULL,
  `RazonSocial` VARCHAR(45) NULL,
  `Direccion` VARCHAR(45) NULL,
  `isAccepted` TINYINT NULL DEFAULT 0,
  `isPostulated` TINYINT NULL DEFAULT 0,
  `isActivated` TINYINT NULL DEFAULT 0,
  `Foto` VARCHAR(45) NULL,
  `MotivoBanneo` VARCHAR(45) NULL,
  `FechaBanneo` TIMESTAMP NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX `fk_Usuario_Rol_idx` (`Rol_idRol` ASC) VISIBLE,
  INDEX `fk_Usuario_Zona1_idx` (`Zona_idZona` ASC) VISIBLE,
  INDEX `fk_Usuario_Distrito1_idx` (`Distrito_idDistrito` ASC) VISIBLE,
  INDEX `fk_Usuario_Usuario1_idx` (`AdmZonal_idUsuario` ASC) VISIBLE,
  INDEX `fk_Usuario_Usuario2_idx` (`AgentCompra_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_Rol`
    FOREIGN KEY (`Rol_idRol`)
    REFERENCES `TRADO_DB`.`Rol` (`idRol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Zona1`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `TRADO_DB`.`Zona` (`idZona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Distrito1`
    FOREIGN KEY (`Distrito_idDistrito`)
    REFERENCES `TRADO_DB`.`Distrito` (`idDistrito`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Usuario1`
    FOREIGN KEY (`AdmZonal_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Usuario2`
    FOREIGN KEY (`AgentCompra_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Token` (
  `idToken` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Tipo` VARCHAR(45) NOT NULL,
  `Duracion` INT NOT NULL,
  `FechaCreacion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idToken`),
  INDEX `fk_Token_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Token_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Proveedor` (
  `idProveedor` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Telefono` VARCHAR(9) NOT NULL,
  `Ruc` VARCHAR(45) NOT NULL,
  `Dni` VARCHAR(8) NOT NULL,
  `Tienda` VARCHAR(45) NOT NULL,
  `isDeleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`idProveedor`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCategoria`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`SubCategoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`SubCategoria` (
  `idSubCategoria` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Categoria_idCategoria` INT NOT NULL,
  PRIMARY KEY (`idSubCategoria`),
  INDEX `fk_SubCategoria_Categoria1_idx` (`Categoria_idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_SubCategoria_Categoria1`
    FOREIGN KEY (`Categoria_idCategoria`)
    REFERENCES `TRADO_DB`.`Categoria` (`idCategoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Producto` (
  `idProducto` INT NOT NULL AUTO_INCREMENT,
  `Proveedor_idProveedor` INT NOT NULL,
  `SubCategoria_idSubCategoria` INT NOT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Cantidad` VARCHAR(45) NOT NULL,
  `FechaArribo` VARCHAR(45) NOT NULL,
  `Descripcion` VARCHAR(45) NOT NULL,
  `Precio` VARCHAR(45) NOT NULL,
  `Color` VARCHAR(45) NULL,
  `Talla` VARCHAR(45) NULL,
  `Material` VARCHAR(45) NULL,
  `Modelo` VARCHAR(45) NULL,
  `Resolucion` VARCHAR(45) NULL,
  `Ram` VARCHAR(45) NULL,
  `Almacenamiento` VARCHAR(45) NULL,
  `isDeleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`idProducto`),
  INDEX `fk_Producto_Proveedor1_idx` (`Proveedor_idProveedor` ASC) VISIBLE,
  INDEX `fk_Producto_SubCategoria1_idx` (`SubCategoria_idSubCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_Proveedor1`
    FOREIGN KEY (`Proveedor_idProveedor`)
    REFERENCES `TRADO_DB`.`Proveedor` (`idProveedor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Producto_SubCategoria1`
    FOREIGN KEY (`SubCategoria_idSubCategoria`)
    REFERENCES `TRADO_DB`.`SubCategoria` (`idSubCategoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`ProductoEnZona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`ProductoEnZona` (
  `Producto_idProducto` INT NOT NULL,
  `Zona_idZona` INT NOT NULL,
  `Cantidad` INT NOT NULL,
  `CostoEnvio` DECIMAL(10,2) NOT NULL,
  `estadoRepo` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`Producto_idProducto`, `Zona_idZona`),
  INDEX `fk_Producto_has_Zona_Zona1_idx` (`Zona_idZona` ASC) VISIBLE,
  INDEX `fk_Producto_has_Zona_Producto1_idx` (`Producto_idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_has_Zona_Producto1`
    FOREIGN KEY (`Producto_idProducto`)
    REFERENCES `TRADO_DB`.`Producto` (`idProducto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Producto_has_Zona_Zona1`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `TRADO_DB`.`Zona` (`idZona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Valoracion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Valoracion` (
  `idValoracion` INT NOT NULL AUTO_INCREMENT,
  `Valor` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idValoracion`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`EstadoOrdenAgente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`EstadoOrdenAgente` (
  `idEstadoOrdenAgente` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstadoOrdenAgente`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`EstadoOrdenImportador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`EstadoOrdenImportador` (
  `idEstadoOrdenImportador` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idEstadoOrdenImportador`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Orden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Orden` (
  `idOrden` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `AgentCompra_idUsuario` INT NULL,
  `Valoracion_idValoracion` INT NULL,
  `EstadoOrdenAgente_idEstadoOrdenAgente` INT NULL,
  `EstadoOrdenImportador_idEstadoOrdenImportador` INT NULL,
  `FechaCreacion` TIMESTAMP NOT NULL,
  `isDeleted` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`idOrden`),
  INDEX `fk_Orden_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Orden_Usuario2_idx` (`AgentCompra_idUsuario` ASC) VISIBLE,
  INDEX `fk_Orden_Valoracion1_idx` (`Valoracion_idValoracion` ASC) VISIBLE,
  INDEX `fk_Orden_EstadoOrdenAgente1_idx` (`EstadoOrdenAgente_idEstadoOrdenAgente` ASC) VISIBLE,
  INDEX `fk_Orden_EstadoOrdenImportador1_idx` (`EstadoOrdenImportador_idEstadoOrdenImportador` ASC) VISIBLE,
  CONSTRAINT `fk_Orden_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Orden_Usuario2`
    FOREIGN KEY (`AgentCompra_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Orden_Valoracion1`
    FOREIGN KEY (`Valoracion_idValoracion`)
    REFERENCES `TRADO_DB`.`Valoracion` (`idValoracion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Orden_EstadoOrdenAgente1`
    FOREIGN KEY (`EstadoOrdenAgente_idEstadoOrdenAgente`)
    REFERENCES `TRADO_DB`.`EstadoOrdenAgente` (`idEstadoOrdenAgente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Orden_EstadoOrdenImportador1`
    FOREIGN KEY (`EstadoOrdenImportador_idEstadoOrdenImportador`)
    REFERENCES `TRADO_DB`.`EstadoOrdenImportador` (`idEstadoOrdenImportador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Chat` (
  `idChat` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Orden_idOrden` INT NOT NULL,
  `Mensaje` VARCHAR(45) NOT NULL,
  `Tiempo` INT NOT NULL,
  PRIMARY KEY (`idChat`),
  INDEX `fk_Chat_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Chat_Orden1_idx` (`Orden_idOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Chat_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Chat_Orden1`
    FOREIGN KEY (`Orden_idOrden`)
    REFERENCES `TRADO_DB`.`Orden` (`idOrden`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`ProductoEnZonaEnOrden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`ProductoEnZonaEnOrden` (
  `ProductoEnZona_Producto_idProducto` INT NOT NULL,
  `ProductoEnZona_Zona_idZona` INT NOT NULL,
  `Orden_idOrden` INT NOT NULL,
  `Cantidad` INT NOT NULL,
  PRIMARY KEY (`ProductoEnZona_Producto_idProducto`, `ProductoEnZona_Zona_idZona`, `Orden_idOrden`),
  INDEX `fk_ProductoEnZona_has_Orden_Orden1_idx` (`Orden_idOrden` ASC) VISIBLE,
  INDEX `fk_ProductoEnZona_has_Orden_ProductoEnZona1_idx` (`ProductoEnZona_Producto_idProducto` ASC, `ProductoEnZona_Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_ProductoEnZona_has_Orden_ProductoEnZona1`
    FOREIGN KEY (`ProductoEnZona_Producto_idProducto` , `ProductoEnZona_Zona_idZona`)
    REFERENCES `TRADO_DB`.`ProductoEnZona` (`Producto_idProducto` , `Zona_idZona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProductoEnZona_has_Orden_Orden1`
    FOREIGN KEY (`Orden_idOrden`)
    REFERENCES `TRADO_DB`.`Orden` (`idOrden`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`ChatBot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`ChatBot` (
  `idChatBot` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Mensaje` VARCHAR(45) NOT NULL,
  `Respuesta` VARCHAR(45) NULL,
  PRIMARY KEY (`idChatBot`),
  INDEX `fk_ChatBot_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_ChatBot_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Notificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Notificacion` (
  `idNotificacion` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Contenido` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idNotificacion`),
  INDEX `fk_Notificacion_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Notificacion_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Pago`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Pago` (
  `idPago` INT NOT NULL AUTO_INCREMENT,
  `Orden_idOrden` INT NOT NULL,
  `Metodo` VARCHAR(45) NOT NULL,
  `Monto` DECIMAL(10,2) NOT NULL,
  `Fecha` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idPago`),
  INDEX `fk_Pago_Orden1_idx` (`Orden_idOrden` ASC) VISIBLE,
  CONSTRAINT `fk_Pago_Orden1`
    FOREIGN KEY (`Orden_idOrden`)
    REFERENCES `TRADO_DB`.`Orden` (`idOrden`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Pregunta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Pregunta` (
  `idPregunta` INT NOT NULL AUTO_INCREMENT,
  `Usuario_idUsuario` INT NOT NULL,
  `Contenido` VARCHAR(80) NOT NULL,
  `Fecha` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idPregunta`),
  INDEX `fk_Consulta_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Consulta_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`Resena`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`Resena` (
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
    REFERENCES `TRADO_DB`.`Producto` (`idProducto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Resena_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`EstadoCodigo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`EstadoCodigo` (
  `idEstadoCodigo` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstadoCodigo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`CodigoDespachador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`CodigoDespachador` (
  `idCodigoDespachador` INT NOT NULL AUTO_INCREMENT,
  `Caracteres` VARCHAR(45) NOT NULL,
  `Distrito_idDistrito` INT NOT NULL,
  `EstadoCodigo_idEstadoCodigo` INT NOT NULL,
  PRIMARY KEY (`idCodigoDespachador`),
  INDEX `fk_CodigoDespachador_Distrito1_idx` (`Distrito_idDistrito` ASC) VISIBLE,
  INDEX `fk_CodigoDespachador_EstadoCodigo1_idx` (`EstadoCodigo_idEstadoCodigo` ASC) VISIBLE,
  CONSTRAINT `fk_CodigoDespachador_Distrito1`
    FOREIGN KEY (`Distrito_idDistrito`)
    REFERENCES `TRADO_DB`.`Distrito` (`idDistrito`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CodigoDespachador_EstadoCodigo1`
    FOREIGN KEY (`EstadoCodigo_idEstadoCodigo`)
    REFERENCES `TRADO_DB`.`EstadoCodigo` (`idEstadoCodigo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `TRADO_DB`.`UsuarioRespuesta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TRADO_DB`.`UsuarioRespuesta` (
  `Pregunta_idPregunta` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  `Contenido` VARCHAR(100) NOT NULL,
  `Fecha` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Pregunta_idPregunta`, `Usuario_idUsuario`),
  INDEX `fk_Pregunta_has_Usuario_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Pregunta_has_Usuario_Pregunta1_idx` (`Pregunta_idPregunta` ASC) VISIBLE,
  CONSTRAINT `fk_Pregunta_has_Usuario_Pregunta1`
    FOREIGN KEY (`Pregunta_idPregunta`)
    REFERENCES `TRADO_DB`.`Pregunta` (`idPregunta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pregunta_has_Usuario_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `TRADO_DB`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
