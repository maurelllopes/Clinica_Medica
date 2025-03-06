-- Adiciona a coluna 'ativo' na tabela 'medicos'
ALTER TABLE medicos ADD ativo TINYINT;

-- Atualiza todos os registros para definir o valor da coluna 'ativo' como 1
UPDATE medicos SET ativo = 1;
