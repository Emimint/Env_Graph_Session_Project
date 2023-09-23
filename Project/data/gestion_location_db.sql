CREATE DATABASE  IF NOT EXISTS `gestion_location` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gestion_location`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: gestion_location
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locations` (
  `id_location` int NOT NULL AUTO_INCREMENT,
  `num_local` varchar(6) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  `superficie` int NOT NULL,
  `annee_construction` int NOT NULL,
  PRIMARY KEY (`id_location`),
  UNIQUE KEY `id_location_UNIQUE` (`id_location`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (1,'101','123 Main Street',1200,1995),(2,'102','456 Elm Avenue',900,2003),(3,'103','789 Oak Road',1500,1990),(4,'104','321 Maple Lane',800,2010),(5,'105','555 Pine Drive',1100,1985),(6,'106','999 Birch Court',950,2005),(7,'107','777 Cedar Street',1300,1998),(8,'108','888 Spruce Avenue',700,2008),(9,'109','222 Redwood Road',1400,2000),(10,'110','333 Sequoia Lane',850,1992),(11,'111','444 Fir Boulevard',1600,1997),(12,'112','666 Willow Way',1000,2007),(13,'113','777 Cherry Street',750,1994),(14,'114','111 Aspen Avenue',1100,2002),(15,'115','222 Poplar Road',950,1988),(16,'116','444 Sycamore Court',1200,1999),(17,'117','333 Cedar Lane',800,2015),(18,'118','555 Birch Drive',1300,1996),(19,'119','666 Spruce Street',700,2009),(20,'120','777 Oak Avenue',1400,1993),(21,'121','888 Maple Road',900,2001),(22,'122','123 Pine Lane',1500,2004),(23,'123','321 Fir Drive',950,2012),(24,'124','555 Elm Boulevard',1100,1991),(25,'125','777 Willow Court',1200,2006),(26,'126','888 Cherry Way',850,1998),(27,'127','999 Redwood Street',1300,2003),(28,'128','333 Sequoia Avenue',700,2011),(29,'129','444 Poplar Road',1400,1995),(30,'130','555 Sycamore Lane',800,2000),(31,'131','666 Cedar Drive',1600,1997),(32,'132','777 Birch Street',1000,2009),(33,'133','888 Spruce Avenue',750,1994),(34,'134','222 Cherry Road',1100,2005),(35,'135','333 Willow Boulevard',950,1993),(36,'136','444 Oak Court',1200,2001),(37,'137','555 Maple Way',800,1999),(38,'138','666 Pine Street',1300,1987),(39,'139','777 Elm Avenue',700,2014),(40,'140','888 Cedar Lane',1400,2008),(41,'141','999 Sycamore Road',900,2002),(42,'142','123 Spruce Drive',1500,1996),(43,'143','321 Fir Avenue',850,2013),(44,'144','555 Willow Road',1100,1992),(45,'145','777 Cherry Boulevard',1200,2007),(46,'146','888 Redwood Lane',750,1990),(47,'147','222 Sequoia Street',1300,2010),(48,'148','333 Poplar Avenue',700,1999),(49,'149','444 Sycamore Drive',1400,2004),(50,'150','555 Cedar Court',950,2006);
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateurs` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(45) NOT NULL,
  `prenom` varchar(45) NOT NULL,
  `mot_de_passe` varchar(45) NOT NULL,
  PRIMARY KEY (`id_utilisateur`),
  UNIQUE KEY `id_utilisateurs_UNIQUE` (`id_utilisateur`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateurs`
--

LOCK TABLES `utilisateurs` WRITE;
/*!40000 ALTER TABLE `utilisateurs` DISABLE KEYS */;
INSERT INTO `utilisateurs` VALUES (1,'Smith','John','tulipe123'),(2,'Johnson','Mary','root1234'),(3,'Davis','Michael','tennis456'),(4,'Wilson','Sarah','football4'),(5,'Brown','David','peche753');
/*!40000 ALTER TABLE `utilisateurs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-23 13:07:33
