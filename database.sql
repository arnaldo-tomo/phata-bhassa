-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 23-Nov-2021 às 09:24
-- Versão do servidor: 10.4.14-MariaDB
-- versão do PHP: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `database`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `payroll`
--

CREATE TABLE `payroll` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `gross` double NOT NULL,
  `over` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `payroll`
--

INSERT INTO `payroll` (`id`, `name`, `gross`, `over`) VALUES
(1, 'chapo Domingos', 500, 48),
(5, 'Dimetrio', 5000, 24),
(26, 'Arnaldo Tomo', 5100, 100),
(77, 'Dimetro', 1000, 192),
(888, 'Tomo', 2600, 2),
(896, 'david Maranho', 5890, 5);

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `payroll`
--
ALTER TABLE `payroll`
  ADD PRIMARY KEY (`id`) USING BTREE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
