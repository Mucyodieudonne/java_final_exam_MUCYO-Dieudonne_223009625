-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 23, 2025 at 01:55 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `iot_assistant_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `alerts`
--

CREATE TABLE `alerts` (
  `alert_id` int(11) NOT NULL,
  `sensor_id` int(11) DEFAULT NULL,
  `alert_type` varchar(50) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `status` enum('unread','read') DEFAULT 'unread',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `alerts`
--

INSERT INTO `alerts` (`alert_id`, `sensor_id`, `alert_type`, `message`, `status`, `created_at`) VALUES
(1, 1, 'Temperature', 'High temperature detected in Huye server room', 'read', '2025-11-07 08:00:00'),
(2, 2, 'Humidity', 'Low humidity detected in Kigali storage', 'unread', '2025-11-07 07:30:00'),
(3, 3, 'Motion', 'Movement detected in Nyamirambo warehouse', 'read', '2025-11-06 16:45:00'),
(4, 4, 'Smoke', 'Smoke detected in Muhanga factory', 'read', '2025-11-06 19:15:00'),
(5, 5, 'Water Level', 'Water level high in Akagera water tank', 'unread', '2025-11-05 05:20:00');

-- --------------------------------------------------------

--
-- Table structure for table `devices`
--

CREATE TABLE `devices` (
  `device_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `device_name` varchar(100) DEFAULT NULL,
  `device_type` varchar(50) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `status` enum('online','offline') DEFAULT 'offline',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `devices`
--

INSERT INTO `devices` (`device_id`, `user_id`, `device_name`, `device_type`, `location`, `status`, `created_at`) VALUES
(1, 1, 'Smart Fan', 'Actuator', 'Bedroom', 'online', '2025-10-28 21:49:40'),
(2, 1, 'Temperature Sensor', 'Sensor', 'Living Room', 'online', '2025-10-29 06:15:22'),
(3, 1, 'Smart Light', 'Actuator', 'Kitchen', 'offline', '2025-10-29 12:32:10'),
(4, 2, 'Door Lock', 'Actuator', 'Front Door', 'online', '2025-10-30 07:45:33'),
(5, 2, 'Motion Detector', 'Sensor', 'Hallway', 'online', '2025-10-30 09:20:18'),
(7, NULL, 'Light Sensor', 'Sensor', NULL, 'offline', '2025-11-20 07:25:11'),
(8, NULL, 'Humidity Sensor', 'Sensor', NULL, 'offline', '2025-11-20 07:25:52');

-- --------------------------------------------------------

--
-- Table structure for table `device_logs`
--

CREATE TABLE `device_logs` (
  `log_id` int(11) NOT NULL,
  `device_id` int(11) DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `performed_by` varchar(100) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `device_logs`
--

INSERT INTO `device_logs` (`log_id`, `device_id`, `action`, `performed_by`, `timestamp`) VALUES
(1, 1, 'Sensor activated (Temperature)', 'System Auto', '2025-11-07 07:12:35'),
(2, 2, 'Device rebooted successfully', 'Admin User', '2025-11-07 07:45:10'),
(3, 3, 'Humidity data sent to server', 'Sensor Node', '2025-11-07 08:03:27'),
(4, 4, 'Motion detected in Huye Lab', 'Security Sensor', '2025-11-07 08:25:52'),
(5, 5, 'Device disconnected (offline)', 'Gateway Hub', '2025-11-07 08:40:11');

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

CREATE TABLE `logs` (
  `log_id` int(11) NOT NULL,
  `device_id` int(11) DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `performed_by` int(11) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `logs`
--

INSERT INTO `logs` (`log_id`, `device_id`, `action`, `performed_by`, `timestamp`) VALUES
(1, 1, 'Device turned ON', 1, '2025-10-28 21:52:15'),
(2, 3, 'Device turned OFF', 1, '2025-10-29 12:45:30'),
(3, 2, 'Temperature reading captured', 1, '2025-10-29 06:30:22'),
(4, 4, 'Door Lock activated', 2, '2025-10-30 07:50:45'),
(5, 1, 'Fan speed adjusted to medium', 1, '2025-10-30 13:20:10'),
(6, 5, 'Motion detected in Hallway', 2, '2025-10-30 09:35:50'),
(7, 3, 'Light brightness set to 75%', 1, '2025-10-31 06:15:33'),
(8, 4, 'Door Lock deactivated', 2, '2025-10-31 16:22:40');

-- --------------------------------------------------------

--
-- Table structure for table `sensors`
--

CREATE TABLE `sensors` (
  `sensor_id` int(11) NOT NULL,
  `device_id` int(11) DEFAULT NULL,
  `sensor_name` varchar(100) DEFAULT NULL,
  `sensor_type` varchar(50) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sensors`
--

INSERT INTO `sensors` (`sensor_id`, `device_id`, `sensor_name`, `sensor_type`, `unit`, `created_at`) VALUES
(1, 1, 'Temperature Sensor', 'DHT11', '°C', '2025-10-28 21:50:28'),
(2, 2, 'Humidity Sensor', 'DHT22', '%', '2025-10-29 06:20:15'),
(3, 3, 'Motion Sensor', 'PIR', 'boolean', '2025-10-29 12:35:42'),
(4, 4, 'Pressure Sensor', 'BMP280', 'hPa', '2025-10-30 07:50:18'),
(5, 5, 'Light Sensor', 'LDR', 'lux', '2025-10-30 09:25:33'),
(6, NULL, 'Temperature Sensor', 'PIR', NULL, '2025-11-07 11:10:01'),
(7, NULL, 'Temperature Sensor', 'PIR', NULL, '2025-11-07 11:10:48');

-- --------------------------------------------------------

--
-- Table structure for table `sensor_data`
--

CREATE TABLE `sensor_data` (
  `data_id` int(11) NOT NULL,
  `sensor_id` int(11) DEFAULT NULL,
  `value` float DEFAULT NULL,
  `recorded_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sensor_data`
--

INSERT INTO `sensor_data` (`data_id`, `sensor_id`, `value`, `recorded_at`) VALUES
(1, 1, 29.5, '2025-10-28 21:51:19'),
(2, 1, 30.2, '2025-10-28 22:15:33'),
(3, 2, 65.5, '2025-10-29 06:25:47'),
(4, 3, 1, '2025-10-29 12:40:12'),
(5, 4, 1013.25, '2025-10-30 07:55:28'),
(6, 5, 450.75, '2025-10-30 09:30:15');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `setting_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `preferred_temperature` float DEFAULT NULL,
  `notification_enabled` tinyint(1) DEFAULT 1,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`setting_id`, `user_id`, `preferred_temperature`, `notification_enabled`, `updated_at`) VALUES
(1, 1, 24, 1, '2025-12-20 07:00:00'),
(2, 2, 22, 0, '2025-12-20 07:15:00'),
(3, 3, 26, 1, '2025-12-20 07:30:00'),
(4, 4, 23, 1, '2025-12-20 07:45:00'),
(5, 5, 25, 0, '2025-12-20 08:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('admin','user') DEFAULT 'user',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'Mucyo Dieudonne', 'dmucyo720@gmail.com ', '12345', 'admin', '2025-10-28 21:48:57'),
(2, 'Dieudonné Mucyo', 'mucyo.dieudonne@iotassistant.rw', '12345', 'user', '2025-11-06 06:00:00'),
(3, 'Alice Uwase', 'alice.uwase@iotassistant.rw', '12345', 'user', '2025-11-06 06:10:00'),
(4, 'Eric Nshimiyimana', 'eric.nshimiyimana@iotassistant.rw', '12345', 'user', '2025-11-06 06:20:00'),
(5, 'Sandrine Mukamana', 'sandrine.mukamana@iotassistant.rw', '12345', 'user', '2025-11-06 06:30:00'),
(6, 'Patrick Habimana', 'patrick.habimana@iotassistant.rw', '12345', 'user', '2025-11-06 06:40:00'),
(7, 'admin', 'admin@example.com', 'admin123', 'admin', '2025-11-20 09:07:18'),
(8, 'NIYIGENA Alice', 'niyigena@gmail.com', '15754', 'user', '2025-11-20 09:44:27'),
(9, 'Theo', 'Theo@gmail.com', '12345', 'admin', '2025-11-20 09:46:03');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alerts`
--
ALTER TABLE `alerts`
  ADD PRIMARY KEY (`alert_id`),
  ADD KEY `sensor_id` (`sensor_id`);

--
-- Indexes for table `devices`
--
ALTER TABLE `devices`
  ADD PRIMARY KEY (`device_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `device_logs`
--
ALTER TABLE `device_logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `device_id` (`device_id`);

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `device_id` (`device_id`),
  ADD KEY `performed_by` (`performed_by`);

--
-- Indexes for table `sensors`
--
ALTER TABLE `sensors`
  ADD PRIMARY KEY (`sensor_id`),
  ADD KEY `device_id` (`device_id`);

--
-- Indexes for table `sensor_data`
--
ALTER TABLE `sensor_data`
  ADD PRIMARY KEY (`data_id`),
  ADD KEY `sensor_id` (`sensor_id`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`setting_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `alerts`
--
ALTER TABLE `alerts`
  MODIFY `alert_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `devices`
--
ALTER TABLE `devices`
  MODIFY `device_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `device_logs`
--
ALTER TABLE `device_logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `logs`
--
ALTER TABLE `logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `sensors`
--
ALTER TABLE `sensors`
  MODIFY `sensor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `sensor_data`
--
ALTER TABLE `sensor_data`
  MODIFY `data_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
  MODIFY `setting_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `alerts`
--
ALTER TABLE `alerts`
  ADD CONSTRAINT `alerts_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `sensors` (`sensor_id`) ON DELETE CASCADE;

--
-- Constraints for table `devices`
--
ALTER TABLE `devices`
  ADD CONSTRAINT `devices_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `device_logs`
--
ALTER TABLE `device_logs`
  ADD CONSTRAINT `device_logs_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`) ON DELETE CASCADE;

--
-- Constraints for table `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`),
  ADD CONSTRAINT `logs_ibfk_2` FOREIGN KEY (`performed_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `sensors`
--
ALTER TABLE `sensors`
  ADD CONSTRAINT `sensors_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `devices` (`device_id`) ON DELETE CASCADE;

--
-- Constraints for table `sensor_data`
--
ALTER TABLE `sensor_data`
  ADD CONSTRAINT `sensor_data_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `sensors` (`sensor_id`) ON DELETE CASCADE;

--
-- Constraints for table `settings`
--
ALTER TABLE `settings`
  ADD CONSTRAINT `settings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
