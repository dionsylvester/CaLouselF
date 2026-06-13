-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 18, 2024 at 05:18 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ooad_lab`
--

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `itemId` varchar(255) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `itemSize` varchar(255) NOT NULL,
  `itemPrice` varchar(255) NOT NULL,
  `itemCategory` varchar(255) NOT NULL,
  `itemStatus` varchar(255) NOT NULL,
  `itemWishlist` varchar(255) NOT NULL,
  `userId` varchar(255) DEFAULT NULL,
  `itemOfferStatus` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`itemId`, `itemName`, `itemSize`, `itemPrice`, `itemCategory`, `itemStatus`, `itemWishlist`, `userId`, `itemOfferStatus`) VALUES
('IT230', 'Modern Jacket', 'XL', '500000', 'Jacket', 'Approved', '', '', ''),
('IT629', 'Supreme Shirt', 'L', '40000', 'Shirt', 'Approved', '', '', ''),
('IT111', 'NVIDIA Bag', 'Fix', '100000', 'Bag', 'Approved', '', '', 'Too low'),
('IT846', 'Demon Slayer Kimono', 'Slim Fit', '100', 'Kimono', 'Approved', '', '', ''),
('IT143', 'Penguin Shirt', 'XS', '2000', 'Shirt', 'Declined', '', '', ''),
('IT832', 'iPhone Shirt', 'XXS', '100', 'Shirt', 'Approved', '', '', 'Too low'),
('IT775', 'iPad Jeans', 'S', '300', 'Jeans', 'Approved', '', '', ''),
('IT595', 'Sunscreen Shirt', 'M', '200', 'Shirt', 'Declined', '', '', 'Namanya terlalu generik'),
('IT878', 'Jas Hitam', 'XL', '200', 'Jas', 'Pending', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `userId` varchar(255) NOT NULL,
  `itemId` varchar(255) NOT NULL,
  `transactionId` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`userId`, `itemId`, `transactionId`) VALUES
('ID970', 'IT230', 'TR733'),
('ID970', 'IT111', 'TR655'),
('ID970', 'IT629', 'TR684'),
('ID970', 'IT230', 'TR436'),
('ID970', 'IT775', 'TR862'),
('ID970', 'IT775', 'TR408'),
('ID970', 'IT832', 'TR362'),
('ID970', 'IT230', 'TR123');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phoneNumber` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `username`, `password`, `phoneNumber`, `address`, `role`) VALUES
('ID082', 'power', 'poiuytr@', '+621029384756', 'Meikarta', 'Seller'),
('ID970', 'noid', 'qpwoeiru$', '+620987654321', 'Aprilkarta', 'Buyer'),
('ID501', 'reze', 'asdfghjk!', '+621234567890', 'Junikarta', 'Buyer');

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

CREATE TABLE `wishlist` (
  `wishlistId` varchar(255) NOT NULL,
  `itemId` varchar(255) NOT NULL,
  `userId` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
