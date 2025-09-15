-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3307
-- Généré le : lun. 15 sep. 2025 à 11:50
-- Version du serveur : 11.3.2-MariaDB
-- Version de PHP : 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `equida`
--

-- --------------------------------------------------------

--
-- Structure de la table `categvente`
--

DROP TABLE IF EXISTS `categvente`;
CREATE TABLE IF NOT EXISTS `categvente` (
  `code` varchar(10) NOT NULL,
  `libelle` varchar(100) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `categvente`
--

INSERT INTO `categvente` (`code`, `libelle`) VALUES
('C01', 'Vente aux enchères'),
('C02', 'Vente de gré à gré');

-- --------------------------------------------------------

--
-- Structure de la table `cheval`
--

DROP TABLE IF EXISTS `cheval`;
CREATE TABLE IF NOT EXISTS `cheval` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `race_id` int(11) DEFAULT NULL,
  `code_sire` varchar(50) DEFAULT NULL,
  `taille` int(11) DEFAULT NULL,
  `poids` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_race` (`race_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `cheval`
--

INSERT INTO `cheval` (`id`, `nom`, `date_naissance`, `race_id`, `code_sire`, `taille`, `poids`) VALUES
(1, 'Eclipse', '2017-03-12', 4, '0808.001.001Z', 168, 520.00),
(2, 'Aztec', '2019-07-04', 4, '0808.002.002Z', 170, 540.00),
(3, 'Orion', '2015-05-23', 5, '0808.003.003Z', 165, 580.00),
(4, 'Tempête de Feu', '2017-03-12', 1, '0808.004.004Z', 162, 510.00),
(5, 'Éclair Noir', '2019-07-04', 2, '0808.005.005Z', 172, 530.00),
(6, 'Vent du Nord', '2015-05-23', 3, '0808.006.006Z', 168, 560.00),
(7, 'Comète', '2018-01-01', 4, '0808.007.007Z', 169, 590.00),
(8, 'Silver Snow', '2020-11-17', 5, '0808.008.008Z', 167, 550.00),
(9, 'Caramel', '2016-06-30', 6, '0808.009.009Z', 164, 500.00),
(10, 'Storm', '2021-10-10', 1, '0808.010.010Z', 166, 505.00),
(11, 'Mustang', '2014-08-03', 2, '0808.011.011Z', 174, 575.00),
(12, 'Rising Sun', '2019-04-22', 3, '0808.012.012Z', 161, 515.00),
(13, 'Phantom', '2016-12-05', 4, '0808.013.013Z', 173, 595.00),
(14, 'Pompon', '2025-07-13', 2, '0808.014.014Z', 169, 525.00),
(15, 'Fleur du désert', '2023-06-30', 6, '0808.015.015Z', 171, 585.00);

-- --------------------------------------------------------

--
-- Structure de la table `chevalparent`
--

DROP TABLE IF EXISTS `chevalparent`;
CREATE TABLE IF NOT EXISTS `chevalparent` (
  `idParent` int(11) NOT NULL,
  `idEnfant` int(11) NOT NULL,
  PRIMARY KEY (`idParent`,`idEnfant`),
  KEY `fk_chevalparent_enfant` (`idEnfant`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `chevalparent`
--

INSERT INTO `chevalparent` (`idParent`, `idEnfant`) VALUES
(1, 2),
(1, 3),
(2, 4),
(3, 5),
(4, 6);

-- --------------------------------------------------------

--
-- Structure de la table `lieu`
--

DROP TABLE IF EXISTS `lieu`;
CREATE TABLE IF NOT EXISTS `lieu` (
  `id` int(11) NOT NULL,
  `ville` varchar(100) NOT NULL,
  `nbBoxes` int(11) DEFAULT NULL,
  `commentaires` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `lieu`
--

INSERT INTO `lieu` (`id`, `ville`, `nbBoxes`, `commentaires`) VALUES
(1, 'Deauville', 120, 'Haras réputé, installations modernes'),
(2, 'Chantilly', 80, 'Centre d’entraînement historique');

-- --------------------------------------------------------

--
-- Structure de la table `lot`
--

DROP TABLE IF EXISTS `lot`;
CREATE TABLE IF NOT EXISTS `lot` (
  `id` int(11) NOT NULL,
  `prixDepart` decimal(12,2) NOT NULL,
  `vente_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vente_id` (`vente_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `lot`
--

INSERT INTO `lot` (`id`, `prixDepart`, `vente_id`) VALUES
(1001, 25000.00, 1),
(1002, 18000.00, 1),
(1003, 30000.00, 2);

-- --------------------------------------------------------

--
-- Structure de la table `race`
--

DROP TABLE IF EXISTS `race`;
CREATE TABLE IF NOT EXISTS `race` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `race`
--

INSERT INTO `race` (`id`, `nom`) VALUES
(1, 'Pur-sang anglais'),
(2, 'Quarter Horse'),
(3, 'Frison'),
(4, 'Andalou'),
(5, 'Lipizzan'),
(6, 'Mustang');

-- --------------------------------------------------------

--
-- Structure de la table `robe`
--

DROP TABLE IF EXISTS `robe`;
CREATE TABLE IF NOT EXISTS `robe` (
  `id` int(10) NOT NULL,
  `libelle` varchar(255) NOT NULL,
  `description` varchar(25555) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `robe`
--

INSERT INTO `robe` (`id`, `libelle`, `description`) VALUES
(1, 'Alezan', 'Poil roux, crins souvent de la même teinte.'),
(2, 'Bai', 'Corps brun avec crins, extrémités et bout du nez noirs.'),
(3, 'Noir', 'Entièrement noir.'),
(4, 'Gris', 'Mélange de poils blancs et foncés, s’éclaircissant avec l’âge.'),
(5, 'Isabelle', 'Poil jaune/sable, crins noirs.'),
(6, 'Pie', 'Grandes taches blanches et colorées.');

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

DROP TABLE IF EXISTS `vente`;
CREATE TABLE IF NOT EXISTS `vente` (
  `id` int(11) NOT NULL,
  `nom` varchar(200) NOT NULL,
  `dateDebutVente` date NOT NULL,
  `categ_code` varchar(10) NOT NULL,
  `lieu_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `categ_code` (`categ_code`),
  KEY `lieu_id` (`lieu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `vente`
--

INSERT INTO `vente` (`id`, `nom`, `dateDebutVente`, `categ_code`, `lieu_id`) VALUES
(1, 'Vente de poulinières printemps', '2024-05-25', 'C01', 1),
(2, 'Vente de yearlings été', '2024-06-15', 'C02', 2);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `cheval`
--
ALTER TABLE `cheval`
  ADD CONSTRAINT `fk_race` FOREIGN KEY (`race_id`) REFERENCES `race` (`id`) ON DELETE SET NULL;

--
-- Contraintes pour la table `chevalparent`
--
ALTER TABLE `chevalparent`
  ADD CONSTRAINT `fk_chevalparent_enfant` FOREIGN KEY (`idEnfant`) REFERENCES `cheval` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_chevalparent_parent` FOREIGN KEY (`idParent`) REFERENCES `cheval` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `lot`
--
ALTER TABLE `lot`
  ADD CONSTRAINT `lot_ibfk_1` FOREIGN KEY (`vente_id`) REFERENCES `vente` (`id`);

--
-- Contraintes pour la table `vente`
--
ALTER TABLE `vente`
  ADD CONSTRAINT `vente_ibfk_1` FOREIGN KEY (`categ_code`) REFERENCES `categvente` (`code`),
  ADD CONSTRAINT `vente_ibfk_2` FOREIGN KEY (`lieu_id`) REFERENCES `lieu` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
