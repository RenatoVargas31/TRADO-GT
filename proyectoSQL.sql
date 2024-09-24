-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema proyecto_gtics
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `proyecto_gtics` DEFAULT CHARACTER SET utf8mb3 ;
USE `proyecto_gtics` ;

-- -----------------------------------------------------
-- Table `proyecto_gtics`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `nombreCategoria` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCategoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`subcategoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`subcategoria` (
  `idsubcategoria` INT NOT NULL,
  `nombreSubcategoria` VARCHAR(45) NOT NULL,
  `categoria_idCategoria` INT NOT NULL,
  PRIMARY KEY (`idsubcategoria`),
  INDEX `fk_subcategoria_categoria_idx` (`categoria_idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_subcategoria_categoria`
    FOREIGN KEY (`categoria_idCategoria`)
    REFERENCES `proyecto_gtics`.`categoria` (`idCategoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `proyecto_gtics`.`estadoorden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`estadoorden` (
  `idEstado` INT NOT NULL,
  `nombreEstado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstado`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`satisfaccion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`satisfaccion` (
  `idForm` INT NOT NULL AUTO_INCREMENT,
  `foto` LONGBLOB NOT NULL,
  `calidad` TINYINT NOT NULL,
  `rapidez` TINYINT NOT NULL,
  `atencion` TINYINT NOT NULL,
  PRIMARY KEY (`idForm`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`zonas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`zonas` (
  `idZona` INT NOT NULL AUTO_INCREMENT,
  `nameZona` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`distritos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`distritos` (
  `idDistrito` INT NOT NULL AUTO_INCREMENT,
  `nameDistrito` VARCHAR(45) NOT NULL,
  `zonas_idZona` INT NOT NULL,
  PRIMARY KEY (`idDistrito`),
  INDEX `fk_distritos_zonas1_idx` (`zonas_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_distritos_zonas1`
    FOREIGN KEY (`zonas_idZona`)
    REFERENCES `proyecto_gtics`.`zonas` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`estadocodigos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`estadocodigos` (
  `idEstado` INT UNSIGNED NOT NULL,
  `nombreEstado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstado`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`roles` (
  `idRoles` INT NOT NULL AUTO_INCREMENT,
  `nombreRol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRoles`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`usuarios` (
  `idUsuarios` INT NOT NULL AUTO_INCREMENT,
  `nombreUsuario` VARCHAR(45) NULL DEFAULT NULL,
  `apellidoUsuario` VARCHAR(45) NULL DEFAULT NULL,
  `dniUsuario` VARCHAR(8) NULL DEFAULT NULL,
  `correoUsuario` VARCHAR(45) NOT NULL,
  `FechaNacimiento` DATE NULL DEFAULT NULL,
  `contraseñaUsuario` VARCHAR(256) NOT NULL,
  `telefonoUsuario` VARCHAR(9) NULL DEFAULT NULL,
  `codigoDespachador` VARCHAR(20) NULL DEFAULT NULL,
  `rucUsuario` VARCHAR(11) NULL DEFAULT NULL,
  `razonSocial` VARCHAR(45) NULL DEFAULT NULL,
  `codigoJurisdiccion` VARCHAR(45) NULL DEFAULT NULL,
  `roles_idRoles1` INT NOT NULL,
  `distritos_idDistrito` INT NULL DEFAULT NULL,
  `solicitudAgente` VARCHAR(100) NULL DEFAULT NULL,
  `estadoCodigoDespachador_idEstado` INT UNSIGNED NULL DEFAULT NULL,
  `is_active` TINYINT NOT NULL DEFAULT 0,
  `idAgente` INT NULL DEFAULT NULL,
  `idCoordinador` INT NULL DEFAULT NULL,
  `isAccepted` TINYINT NOT NULL DEFAULT 0,
  `zonas_idZona` INT NULL,
  PRIMARY KEY (`idUsuarios`),
  INDEX `fk_usuarios_roles1_idx` (`roles_idRoles1` ASC) VISIBLE,
  INDEX `fk_usuarios_distritos1_idx` (`distritos_idDistrito` ASC) VISIBLE,
  INDEX `fk_usuarios_estadoCodigoDespachador1_idx` (`estadoCodigoDespachador_idEstado` ASC) VISIBLE,
  INDEX `fk_usuarios_usuarios1_idx` (`idAgente` ASC) VISIBLE,
  INDEX `fk_usuarios_usuarios2_idx` (`idCoordinador` ASC) VISIBLE,
  INDEX `fk_usuarios_zonas1_idx` (`zonas_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_usuarios_distritos1`
    FOREIGN KEY (`distritos_idDistrito`)
    REFERENCES `proyecto_gtics`.`distritos` (`idDistrito`),
  CONSTRAINT `fk_usuarios_estadoCodigoDespachador1`
    FOREIGN KEY (`estadoCodigoDespachador_idEstado`)
    REFERENCES `proyecto_gtics`.`estadocodigos` (`idEstado`),
  CONSTRAINT `fk_usuarios_roles1`
    FOREIGN KEY (`roles_idRoles1`)
    REFERENCES `proyecto_gtics`.`roles` (`idRoles`),
  CONSTRAINT `fk_usuarios_usuarios1`
    FOREIGN KEY (`idAgente`)
    REFERENCES `proyecto_gtics`.`usuarios` (`idUsuarios`),
  CONSTRAINT `fk_usuarios_usuarios2`
    FOREIGN KEY (`idCoordinador`)
    REFERENCES `proyecto_gtics`.`usuarios` (`idUsuarios`),
  CONSTRAINT `fk_usuarios_zonas1`
    FOREIGN KEY (`zonas_idZona`)
    REFERENCES `proyecto_gtics`.`zonas` (`idZona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`orden`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`orden` (
  `idOrden` VARCHAR(45) NOT NULL,
  `usuarios_idUsuarios` INT NOT NULL,
  `satisfaccion_idForm` INT NOT NULL,
  `estadoOrden_idEstado` INT NOT NULL,
  `carrito` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`idOrden`),
  INDEX `fk_orden_usuarios1_idx` (`usuarios_idUsuarios` ASC) VISIBLE,
  INDEX `fk_orden_satisfaccion1_idx` (`satisfaccion_idForm` ASC) VISIBLE,
  INDEX `fk_orden_estadoOrden1_idx` (`estadoOrden_idEstado` ASC) VISIBLE,
  CONSTRAINT `fk_orden_estadoOrden1`
    FOREIGN KEY (`estadoOrden_idEstado`)
    REFERENCES `proyecto_gtics`.`estadoorden` (`idEstado`),
  CONSTRAINT `fk_orden_satisfaccion1`
    FOREIGN KEY (`satisfaccion_idForm`)
    REFERENCES `proyecto_gtics`.`satisfaccion` (`idForm`),
  CONSTRAINT `fk_orden_usuarios1`
    FOREIGN KEY (`usuarios_idUsuarios`)
    REFERENCES `proyecto_gtics`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`chat` (
  `idMensajes` INT NOT NULL AUTO_INCREMENT,
  `orden_idOrden` VARCHAR(45) NOT NULL,
  `mensaje` VARCHAR(500) NOT NULL,
  `fechaCreacion` DATETIME NOT NULL,
  `usuarios_idUsuarios` INT NOT NULL,
  PRIMARY KEY (`idMensajes`),
  INDEX `fk_chat_orden1_idx` (`orden_idOrden` ASC) VISIBLE,
  INDEX `fk_chat_usuarios1_idx` (`usuarios_idUsuarios` ASC) VISIBLE,
  CONSTRAINT `fk_chat_orden1`
    FOREIGN KEY (`orden_idOrden`)
    REFERENCES `proyecto_gtics`.`orden` (`idOrden`),
  CONSTRAINT `fk_chat_usuarios1`
    FOREIGN KEY (`usuarios_idUsuarios`)
    REFERENCES `proyecto_gtics`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`notificaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`notificaciones` (
  `idNotificaciones` INT NOT NULL AUTO_INCREMENT,
  `horaCreacion` DATETIME NOT NULL,
  `contenido` VARCHAR(500) NOT NULL,
  `cabecera` VARCHAR(100) NOT NULL,
  `usuarios_idUsuarios` INT NOT NULL,
  PRIMARY KEY (`idNotificaciones`),
  INDEX `fk_notificaciones_usuarios1_idx` (`usuarios_idUsuarios` ASC) VISIBLE,
  CONSTRAINT `fk_notificaciones_usuarios1`
    FOREIGN KEY (`usuarios_idUsuarios`)
    REFERENCES `proyecto_gtics`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`proveedores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`proveedores` (
  `id_proveedor` INT NOT NULL AUTO_INCREMENT,
  `nombreProveedor` VARCHAR(45) NOT NULL,
  `telefonoProveedor` VARCHAR(9) NOT NULL,
  `rucProveedor` VARCHAR(11) NOT NULL,
  `dniProveedor` VARCHAR(8) NOT NULL,
  `nombreTienda` VARCHAR(45) NOT NULL,
  `enabled` TINYINT NOT NULL,
  PRIMARY KEY (`id_proveedor`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`producto` (
  `idProducto` INT NOT NULL AUTO_INCREMENT,
  `nombreProducto` VARCHAR(45) NOT NULL,
  `desccripcion` VARCHAR(250) NOT NULL,
  `precio` DECIMAL(8,2) NOT NULL,
  `costoEnvio` DECIMAL(8,2) NOT NULL,
  `proveedores_id_proveedor` INT NOT NULL,
  `paisOrigen` VARCHAR(45) NULL,
  `colores` VARCHAR(45) NULL,
  `talla` VARCHAR(45) NULL,
  `material` VARCHAR(45) NULL,
  `modelo` VARCHAR(45) NULL,
  `resolucion` VARCHAR(45) NULL,
  `ram` INT NULL,
  `almacenamiento` INT NULL,
  `categoria_idCategoria` INT NOT NULL,
  PRIMARY KEY (`idProducto`),
  INDEX `fk_producto_proveedores1_idx` (`proveedores_id_proveedor` ASC) VISIBLE,
  INDEX `fk_producto_categoria1_idx` (`categoria_idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_producto_proveedores1`
    FOREIGN KEY (`proveedores_id_proveedor`)
    REFERENCES `proyecto_gtics`.`proveedores` (`id_proveedor`),
  CONSTRAINT `fk_producto_categoria1`
    FOREIGN KEY (`categoria_idCategoria`)
    REFERENCES `proyecto_gtics`.`categoria` (`idCategoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`zonas_has_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`zonas_has_producto` (
  `zonas_idZona` INT NOT NULL,
  `producto_idProducto` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `arribo` DATE NULL DEFAULT NULL,
  `bajoStock` TINYINT NOT NULL,
  PRIMARY KEY (`zonas_idZona`, `producto_idProducto`),
  INDEX `fk_zonas_has_producto_producto1_idx` (`producto_idProducto` ASC) VISIBLE,
  INDEX `fk_zonas_has_producto_zonas1_idx` (`zonas_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_zonas_has_producto_producto1`
    FOREIGN KEY (`producto_idProducto`)
    REFERENCES `proyecto_gtics`.`producto` (`idProducto`),
  CONSTRAINT `fk_zonas_has_producto_zonas1`
    FOREIGN KEY (`zonas_idZona`)
    REFERENCES `proyecto_gtics`.`zonas` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`producto_porordeny_por_zona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`producto_porordeny_por_zona` (
  `zonas_has_producto_zonas_idZona` INT NOT NULL,
  `zonas_has_producto_producto_idProducto` INT NOT NULL,
  `orden_idOrden` VARCHAR(45) NOT NULL,
  `cantidad` INT NOT NULL,
  PRIMARY KEY (`zonas_has_producto_zonas_idZona`, `zonas_has_producto_producto_idProducto`, `orden_idOrden`),
  INDEX `fk_zonas_has_producto_has_orden_orden1_idx` (`orden_idOrden` ASC) VISIBLE,
  INDEX `fk_zonas_has_producto_has_orden_zonas_has_producto1_idx` (`zonas_has_producto_zonas_idZona` ASC, `zonas_has_producto_producto_idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_zonas_has_producto_has_orden_orden1`
    FOREIGN KEY (`orden_idOrden`)
    REFERENCES `proyecto_gtics`.`orden` (`idOrden`),
  CONSTRAINT `fk_zonas_has_producto_has_orden_zonas_has_producto1`
    FOREIGN KEY (`zonas_has_producto_zonas_idZona` , `zonas_has_producto_producto_idProducto`)
    REFERENCES `proyecto_gtics`.`zonas_has_producto` (`zonas_idZona` , `producto_idProducto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `proyecto_gtics`.`token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proyecto_gtics`.`token` (
  `idToken` INT NOT NULL AUTO_INCREMENT,
  `fecha` TIMESTAMP NOT NULL,
  `tiempo` INT NOT NULL,
  `usuarios_idUsuarios` INT NOT NULL,
  PRIMARY KEY (`idToken`),
  INDEX `fk_token_usuarios1_idx` (`usuarios_idUsuarios` ASC) VISIBLE,
  CONSTRAINT `fk_token_usuarios1`
    FOREIGN KEY (`usuarios_idUsuarios`)
    REFERENCES `proyecto_gtics`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
