SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `genesisdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `Attachments`
--

CREATE TABLE `Attachments` (
  `id` bigint NOT NULL,
  `contentType` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `size` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `BinaryFiles`
--

CREATE TABLE `BinaryFiles` (
  `data` longblob,
  `attachment_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `Models`
--

CREATE TABLE `Models` (
  `id` bigint NOT NULL,
  `uploadDate` datetime(6) DEFAULT NULL,
  `attachment_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Products`
--

CREATE TABLE `Products` (
  `id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `uploadedBy` varchar(255) NOT NULL,
  `model_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Products_Attachments`
--

CREATE TABLE `Products_Attachments` (
  `Product_id` bigint NOT NULL,
  `photos_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Attachments`
--
ALTER TABLE `Attachments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_20ov5wxnt9f69nr0uqedixgfq` (`reference`);

--
-- Indexes for table `BinaryFiles`
--
ALTER TABLE `BinaryFiles`
  ADD PRIMARY KEY (`attachment_id`);

--
-- Indexes for table `Models`
--
ALTER TABLE `Models`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpikmfsq8pugm7vvahljxigtij` (`attachment_id`);

--
-- Indexes for table `Products`
--
ALTER TABLE `Products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_product_name` (`name`),
  ADD KEY `idx_product_description` (`description`),
  ADD KEY `FK8yrwxmolj9l2n79nbmm7g49ec` (`model_id`);

--
-- Indexes for table `Products_Attachments`
--
ALTER TABLE `Products_Attachments`
  ADD UNIQUE KEY `UK_3s58qh0497kk49q5r06jdbhwu` (`photos_id`),
  ADD KEY `FKn3bqibt3cc1yb5124w8xsja09` (`Product_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `BinaryFiles`
--
ALTER TABLE `BinaryFiles`
  ADD CONSTRAINT `FK5k6guyx7i00881o36yhohye31` FOREIGN KEY (`attachment_id`) REFERENCES `Attachments` (`id`);

--
-- Constraints for table `Models`
--
ALTER TABLE `Models`
  ADD CONSTRAINT `FKpikmfsq8pugm7vvahljxigtij` FOREIGN KEY (`attachment_id`) REFERENCES `Attachments` (`id`);

--
-- Constraints for table `Products`
--
ALTER TABLE `Products`
  ADD CONSTRAINT `FK8yrwxmolj9l2n79nbmm7g49ec` FOREIGN KEY (`model_id`) REFERENCES `Models` (`id`);

--
-- Constraints for table `Products_Attachments`
--
ALTER TABLE `Products_Attachments`
  ADD CONSTRAINT `FKn3bqibt3cc1yb5124w8xsja09` FOREIGN KEY (`Product_id`) REFERENCES `Products` (`id`),
  ADD CONSTRAINT `FKr0mpdfdp9om0qli242sbcds3u` FOREIGN KEY (`photos_id`) REFERENCES `Attachments` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
