ALTER TABLE `person`
    ADD COLUMN `enabled` BIT(1) NOT NULL DEFAULT 1 AFTER `gender`;