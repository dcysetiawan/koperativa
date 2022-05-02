-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 30, 2021 at 02:07 PM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `koperativa`
--

-- --------------------------------------------------------

--
-- Stand-in structure for view `kview_cashiers`
-- (See below for the actual view)
--
CREATE TABLE `kview_cashiers` (
`user_id` varchar(256)
,`username` varchar(256)
,`password` varchar(256)
,`display_name` varchar(256)
,`role_id` int(11)
,`created_at` datetime
,`is_blocked` tinyint(1)
,`summary` decimal(41,0)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `kview_categories`
-- (See below for the actual view)
--
CREATE TABLE `kview_categories` (
`category_id` int(11)
,`category_name` varchar(256)
,`added_at` datetime
,`is_deleted` tinyint(1)
,`total_product` bigint(21)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `kview_products`
-- (See below for the actual view)
--
CREATE TABLE `kview_products` (
`product_id` varchar(256)
,`category_id` int(11)
,`category_name` varchar(256)
,`product_name` varchar(256)
,`product_stock` int(11)
,`product_price` bigint(20)
,`added_at` datetime
,`is_deleted` tinyint(1)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `kview_transactions`
-- (See below for the actual view)
--
CREATE TABLE `kview_transactions` (
`custom_id` varchar(12)
,`transaction_id` int(11)
,`user_id` varchar(256)
,`display_name` varchar(256)
,`total` bigint(20)
,`customer_money` bigint(20)
,`change_money` bigint(20)
,`datetime` varchar(20)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `kview_transactions_detail`
-- (See below for the actual view)
--
CREATE TABLE `kview_transactions_detail` (
`detail_id` int(11)
,`transaction_id` int(11)
,`product_name` varchar(256)
,`product_qty` int(11)
,`product_price` bigint(20)
,`product_subtotal` bigint(30)
);

-- --------------------------------------------------------

--
-- Table structure for table `kv_categories`
--

CREATE TABLE `kv_categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(256) NOT NULL,
  `added_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kv_categories`
--

INSERT INTO `kv_categories` (`category_id`, `category_name`, `added_at`, `is_deleted`) VALUES
(1, 'Alat Tulis Kantor', '2021-01-28 11:53:15', 0),
(2, 'Atribut Sekolah', '2021-01-29 11:53:15', 0),
(3, 'Makanan', '2021-01-29 13:59:18', 0),
(4, 'Minuman', '2021-01-29 16:59:33', 1),
(5, 'test', '2021-01-30 18:27:25', 1),
(6, 'test', '2021-01-30 19:47:14', 1),
(7, 'test', '2021-01-30 19:55:02', 1);

-- --------------------------------------------------------

--
-- Table structure for table `kv_products`
--

CREATE TABLE `kv_products` (
  `product_id` varchar(256) NOT NULL,
  `category_id` int(11) NOT NULL,
  `product_name` varchar(256) NOT NULL,
  `product_stock` int(11) NOT NULL,
  `product_price` bigint(20) NOT NULL,
  `added_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kv_products`
--

INSERT INTO `kv_products` (`product_id`, `category_id`, `product_name`, `product_stock`, `product_price`, `added_at`, `is_deleted`) VALUES
('00800200301', 1, 'Pulpen Snowman V-3', 5, 3400, '2021-01-28 11:55:45', 0),
('00800200302', 2, 'Topi Sekolah', 22, 8500, '2021-01-28 11:55:46', 0),
('00800200303', 2, 'Dasi Sekolah', 27, 7500, '2021-01-29 15:06:26', 0),
('00800200304', 3, 'Chocolatos Wafer', 29, 2000, '2021-01-29 15:17:20', 0),
('00800200305', 2, 'Ikat Pinggang', 50, 9000, '2021-01-30 19:54:32', 0);

-- --------------------------------------------------------

--
-- Table structure for table `kv_roles`
--

CREATE TABLE `kv_roles` (
  `role_id` int(11) NOT NULL,
  `role_desc` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kv_roles`
--

INSERT INTO `kv_roles` (`role_id`, `role_desc`) VALUES
(1, 'Admin'),
(2, 'Cashier');

-- --------------------------------------------------------

--
-- Table structure for table `kv_transactions`
--

CREATE TABLE `kv_transactions` (
  `transaction_id` int(11) NOT NULL,
  `user_id` varchar(256) NOT NULL COMMENT 'Kasir',
  `total` bigint(20) NOT NULL,
  `customer_money` bigint(20) NOT NULL,
  `change_money` bigint(20) NOT NULL,
  `datetime` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kv_transactions`
--

INSERT INTO `kv_transactions` (`transaction_id`, `user_id`, `total`, `customer_money`, `change_money`, `datetime`) VALUES
(2, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 40800, 45000, 4200, '2021-01-20 13:29:13'),
(3, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 93500, 100000, 6500, '2021-01-26 14:44:44'),
(4, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 23800, 25000, 1200, '2021-01-28 16:11:37'),
(5, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 18700, 20000, 1300, '2021-01-28 17:21:59'),
(6, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 28900, 30000, 1100, '2021-01-29 11:40:24'),
(7, 'e2ac6adb-3039-40cc-84c9-10aafec24d8d', 36800, 40000, 3200, '2021-01-29 17:06:38'),
(8, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 74200, 75000, 800, '2021-01-30 18:25:54'),
(9, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 64500, 66000, 1500, '2021-01-30 19:12:44'),
(10, 'a3821d23-60e2-425b-a5e9-fd9e4752757d', 37500, 40000, 2500, '2021-01-30 19:49:57');

-- --------------------------------------------------------

--
-- Table structure for table `kv_transactions_detail`
--

CREATE TABLE `kv_transactions_detail` (
  `detail_id` int(11) NOT NULL,
  `transaction_id` int(11) NOT NULL,
  `product_name` varchar(256) NOT NULL,
  `product_qty` int(11) NOT NULL,
  `product_price` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kv_transactions_detail`
--

INSERT INTO `kv_transactions_detail` (`detail_id`, `transaction_id`, `product_name`, `product_qty`, `product_price`) VALUES
(1, 2, 'Pulpen Snowman V-3', 12, 3400),
(2, 3, 'Pulpen Snowman V-3', 10, 3400),
(3, 3, 'Topi Sekolah', 7, 8500),
(4, 4, 'Pulpen Snowman V-3', 2, 3400),
(5, 4, 'Topi Sekolah', 2, 8500),
(6, 5, 'Pulpen Snowman V-3', 3, 3400),
(7, 5, 'Topi Sekolah', 1, 8500),
(8, 6, 'Pulpen Snowman V-3', 6, 3400),
(9, 6, 'Topi Sekolah', 1, 8500),
(10, 7, 'Dasi Sekolah', 4, 7500),
(11, 7, 'Pulpen Snowman V-3', 2, 3400),
(12, 8, 'Pulpen Snowman V-3', 13, 3400),
(13, 8, 'Dasi Sekolah', 4, 7500),
(14, 9, 'Pulpen Snowman V-3', 10, 3400),
(15, 9, 'Dasi Sekolah', 3, 7500),
(16, 9, 'Chocolatos Wafer', 4, 2000),
(17, 10, 'Dasi Sekolah', 5, 7500);

-- --------------------------------------------------------

--
-- Table structure for table `kv_users`
--

CREATE TABLE `kv_users` (
  `user_id` varchar(256) NOT NULL,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `display_name` varchar(256) NOT NULL,
  `role_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `is_blocked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kv_users`
--

INSERT INTO `kv_users` (`user_id`, `username`, `password`, `display_name`, `role_id`, `created_at`, `is_blocked`) VALUES
('44f95d44-a33a-4eab-9c0d-82e518e4b366', 'admin', '60n0IJ1EhuQ=', 'Administrator', 1, '2021-01-28 11:36:02', 0),
('45b885f7-4d18-498a-801c-f008e32d31f7', 'cashier2', 'PAPweb107mcMEOqMqXqehQ==', 'Cashier 2', 2, '2021-01-29 12:08:46', 1),
('49cc84cd-585b-450d-a414-ca6d957d27f7', 'cashier3', 'd2LjhZHs60cMEOqMqXqehQ==', 'Cashier 3', 2, '2021-01-29 14:44:32', 0),
('a3821d23-60e2-425b-a5e9-fd9e4752757d', 'cashier1', 'ANUHhw9NOwsMEOqMqXqehQ==', 'Cashier 1', 2, '2021-01-28 11:37:04', 0),
('e2ac6adb-3039-40cc-84c9-10aafec24d8d', 'cashier4', 'TO9C/blnmQcMEOqMqXqehQ==', 'Cashier 4', 2, '2021-01-29 17:02:20', 0);

-- --------------------------------------------------------

--
-- Structure for view `kview_cashiers`
--
DROP TABLE IF EXISTS `kview_cashiers`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dicky`@`localhost` SQL SECURITY DEFINER VIEW `kview_cashiers`  AS SELECT `cashiers`.`user_id` AS `user_id`, `cashiers`.`username` AS `username`, `cashiers`.`password` AS `password`, `cashiers`.`display_name` AS `display_name`, `cashiers`.`role_id` AS `role_id`, `cashiers`.`created_at` AS `created_at`, `cashiers`.`is_blocked` AS `is_blocked`, sum(`transactions`.`total`) AS `summary` FROM (`kv_users` `cashiers` left join `kv_transactions` `transactions` on(`cashiers`.`user_id` = `transactions`.`user_id`)) WHERE `cashiers`.`role_id` = 2 GROUP BY `cashiers`.`user_id` ORDER BY `cashiers`.`created_at` DESC ;

-- --------------------------------------------------------

--
-- Structure for view `kview_categories`
--
DROP TABLE IF EXISTS `kview_categories`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dicky`@`localhost` SQL SECURITY DEFINER VIEW `kview_categories`  AS SELECT `categories`.`category_id` AS `category_id`, `categories`.`category_name` AS `category_name`, `categories`.`added_at` AS `added_at`, `categories`.`is_deleted` AS `is_deleted`, count(`products`.`product_id`) AS `total_product` FROM (`kv_categories` `categories` left join `kv_products` `products` on(`products`.`category_id` = `categories`.`category_id`)) GROUP BY `categories`.`category_id` ORDER BY `categories`.`added_at` DESC ;

-- --------------------------------------------------------

--
-- Structure for view `kview_products`
--
DROP TABLE IF EXISTS `kview_products`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dicky`@`localhost` SQL SECURITY DEFINER VIEW `kview_products`  AS SELECT `products`.`product_id` AS `product_id`, `products`.`category_id` AS `category_id`, `categories`.`category_name` AS `category_name`, `products`.`product_name` AS `product_name`, `products`.`product_stock` AS `product_stock`, `products`.`product_price` AS `product_price`, `products`.`added_at` AS `added_at`, `products`.`is_deleted` AS `is_deleted` FROM (`kv_products` `products` join `kv_categories` `categories` on(`products`.`category_id` = `categories`.`category_id`)) ORDER BY `products`.`added_at` DESC ;

-- --------------------------------------------------------

--
-- Structure for view `kview_transactions`
--
DROP TABLE IF EXISTS `kview_transactions`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dicky`@`localhost` SQL SECURITY DEFINER VIEW `kview_transactions`  AS SELECT concat(date_format(`transactions`.`datetime`,'%y%m%d'),substr(concat('00000',`transactions`.`transaction_id`),-6)) AS `custom_id`, `transactions`.`transaction_id` AS `transaction_id`, `transactions`.`user_id` AS `user_id`, `users`.`display_name` AS `display_name`, `transactions`.`total` AS `total`, `transactions`.`customer_money` AS `customer_money`, `transactions`.`change_money` AS `change_money`, date_format(`transactions`.`datetime`,'%Y-%m-%d, %h:%m:%s') AS `datetime` FROM (`kv_transactions` `transactions` join `kv_users` `users` on(`transactions`.`user_id` = `users`.`user_id`)) ORDER BY `transactions`.`transaction_id` DESC ;

-- --------------------------------------------------------

--
-- Structure for view `kview_transactions_detail`
--
DROP TABLE IF EXISTS `kview_transactions_detail`;

CREATE ALGORITHM=UNDEFINED DEFINER=`dicky`@`localhost` SQL SECURITY DEFINER VIEW `kview_transactions_detail`  AS SELECT `kv_transactions_detail`.`detail_id` AS `detail_id`, `kv_transactions_detail`.`transaction_id` AS `transaction_id`, `kv_transactions_detail`.`product_name` AS `product_name`, `kv_transactions_detail`.`product_qty` AS `product_qty`, `kv_transactions_detail`.`product_price` AS `product_price`, `kv_transactions_detail`.`product_price`* `kv_transactions_detail`.`product_qty` AS `product_subtotal` FROM `kv_transactions_detail` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kv_categories`
--
ALTER TABLE `kv_categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `kv_products`
--
ALTER TABLE `kv_products`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `kv_roles`
--
ALTER TABLE `kv_roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `kv_transactions`
--
ALTER TABLE `kv_transactions`
  ADD PRIMARY KEY (`transaction_id`);

--
-- Indexes for table `kv_transactions_detail`
--
ALTER TABLE `kv_transactions_detail`
  ADD PRIMARY KEY (`detail_id`);

--
-- Indexes for table `kv_users`
--
ALTER TABLE `kv_users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kv_categories`
--
ALTER TABLE `kv_categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `kv_roles`
--
ALTER TABLE `kv_roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `kv_transactions`
--
ALTER TABLE `kv_transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `kv_transactions_detail`
--
ALTER TABLE `kv_transactions_detail`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
