-- ==============================================================================
-- 0. ROLES (Pré-requisito para Usuários)
-- ==============================================================================
INSERT INTO role (nome) VALUES
    ('GESTOR'),
    ('INSTRUTOR'),
    ('ALUNO');

-- ==============================================================================
-- 1. PLANOS DE ASSINATURA
-- ==============================================================================
INSERT INTO planos (nome, valor, duracao_meses, ativo) VALUES
    ('Plano Mensal', 99.90, 1, true),
    ('Plano Trimestral', 269.70, 3, true),
    ('Plano Semestral', 499.90, 6, true),
    ('Plano Anual VIP', 899.00, 12, true);

-- ==============================================================================
-- 2. USUÁRIOS
-- Senha padrão para 'Devfit123': $2a$10$QQ/JF/RuTz5OvTl5jG6gIOQsp8qPhU743noXYaDzX944lg3WE1IpG
-- ==============================================================================
INSERT INTO usuario (nome, email, senha) VALUES
    ('Muliro Gestor', 'admin@devfit.com', '$2a$10$QQ/JF/RuTz5OvTl5jG6gIOQsp8qPhU743noXYaDzX944lg3WE1IpG'),
    ('Paulo Instrutor', 'instrutor@devfit.com', '$2a$10$QQ/JF/RuTz5OvTl5jG6gIOQsp8qPhU743noXYaDzX944lg3WE1IpG'),
    ('Ana Aluna', 'ana@email.com', '$2a$10$QQ/JF/RuTz5OvTl5jG6gIOQsp8qPhU743noXYaDzX944lg3WE1IpG'),
    ('João Maromba', 'joao@email.com', '$2a$10$QQ/JF/RuTz5OvTl5jG6gIOQsp8qPhU743noXYaDzX944lg3WE1IpG');

-- VINCULAR ROLES AOS USUÁRIOS
INSERT INTO usuario_roles (usuario_id, role_id) VALUES
    ((SELECT id FROM usuario WHERE email='admin@devfit.com'), (SELECT id FROM role WHERE nome='GESTOR')),
    ((SELECT id FROM usuario WHERE email='instrutor@devfit.com'), (SELECT id FROM role WHERE nome='INSTRUTOR')),
    ((SELECT id FROM usuario WHERE email='ana@email.com'), (SELECT id FROM role WHERE nome='ALUNO')),
    ((SELECT id FROM usuario WHERE email='joao@email.com'), (SELECT id FROM role WHERE nome='ALUNO'));

-- ==============================================================================
-- 3. MATRÍCULAS E MENSALIDADES
-- ==============================================================================
-- Matrícula para Ana (Ativa)
INSERT INTO matriculas (plano_id, data_inicio, data_vencimento, is_ativa) VALUES
    ((SELECT id FROM planos WHERE duracao_meses=6), CURRENT_DATE - 30, CURRENT_DATE + 150, true);

-- Vincular matrícula ao usuário Ana
UPDATE usuario SET matricula_id = (SELECT MAX(id) FROM matriculas) WHERE email='ana@email.com';

-- Mensalidades pagas (Histórico financeiro)
INSERT INTO mensalidades (usuario_id, valor_pago, data_pagamento) VALUES
    ((SELECT id FROM usuario WHERE email='ana@email.com'), 499.90, CURRENT_DATE - 30),
    ((SELECT id FROM usuario WHERE email='joao@email.com'), 99.90, CURRENT_DATE - 5);

-- ==============================================================================
-- 4. EQUIPAMENTOS
-- ==============================================================================
INSERT INTO equipamento (nome, descricao, quantidade, valor, data_aquisicao, status) VALUES
    ('Esteira Movement', 'Esteira profissional modelo X', 10, 15000.00, '2023-01-15', 'ATIVO'),
    ('Supino Reto', 'Banco de supino olímpico', 4, 2500.00, '2023-02-10', 'ATIVO'),
    ('Cadeira Extensora', 'Máquina de quadríceps', 2, 8000.00, '2023-03-20', 'MANUTENCAO'),
    ('Halteres 10kg', 'Par de halteres emborrachados', 20, 250.00, '2023-01-10', 'ATIVO');

-- ==============================================================================
-- 5. EXERCÍCIOS
-- ==============================================================================
INSERT INTO exercicio (nome, musculo_principal, descricao) VALUES
    ('Agachamento Livre', 'Pernas', 'Agachamento com barra nas costas'),
    ('Supino Reto', 'Peito', 'Deitado no banco, empurrar a barra para cima'),
    ('Puxada Alta', 'Costas', 'Puxada na polia alta pela frente'),
    ('Rosca Direta', 'Bíceps', 'Flexão de cotovelos com barra em pé'),
    ('Tríceps Corda', 'Tríceps', 'Extensão de cotovelos na polia'),
    ('Corrida na Esteira', 'Cardio', 'Corrida moderada por tempo determinado');

-- ==============================================================================
-- 6. FICHAS DE TREINO E ITENS
-- ==============================================================================
-- Ficha para o João, criada pelo Instrutor Paulo
INSERT INTO ficha_treino (aluno_id, instrutor_id, data_criacao, data_vencimento, is_ativa) VALUES
    ((SELECT id FROM usuario WHERE email='joao@email.com'),
     (SELECT id FROM usuario WHERE email='instrutor@devfit.com'),
     CURRENT_DATE,
     CURRENT_DATE + 45,
     true);

-- Itens da ficha
INSERT INTO item_treino (ficha_treino_id, exercicio_base_id, series, repeticoes, carga_estimada_kg, observacoes) VALUES
    ((SELECT MAX(id) FROM ficha_treino), (SELECT id FROM exercicio WHERE nome='Supino Reto'), 4, 10, 30.0, 'Controlar descida'),
    ((SELECT MAX(id) FROM ficha_treino), (SELECT id FROM exercicio WHERE nome='Tríceps Corda'), 3, 12, 15.0, 'Falha concêntrica'),
    ((SELECT MAX(id) FROM ficha_treino), (SELECT id FROM exercicio WHERE nome='Corrida na Esteira'), 1, 1, 0, '20 minutos pós treino');

-- ==============================================================================
-- 7. AVALIAÇÃO FÍSICA
-- ==============================================================================
INSERT INTO ficha_avaliacao (
    aluno_id, instrutor_id, data_avaliacao, peso_kg, altura_cm, imc,
    circunferencia_cintura_cm, circunferencia_abdomen_cm, circunferencia_quadril_cm,
    historico_saude, observacoes_gerais
) VALUES (
    (SELECT id FROM usuario WHERE email='ana@email.com'),
    (SELECT id FROM usuario WHERE email='instrutor@devfit.com'),
    CURRENT_DATE - 10,
    62.5, 165, 22.9,
    68.0, 72.0, 98.0,
    'Sem lesões recentes. Relata dor leve no joelho.',
    'Aluna com bom condicionamento, focar em hipertrofia.'
);

-- ==============================================================================
-- 8. FINANCEIRO (DESPESAS E PEDIDOS)
-- ==============================================================================
INSERT INTO despesas (categoria, valor, data_vencimento) VALUES
    ('ALUGUEL', 3500.00, CURRENT_DATE + 5),
    ('SALARIOS', 12000.00, CURRENT_DATE + 5),
    ('LUZ', 850.00, CURRENT_DATE + 10),
    ('MANUTENCAO', 400.00, CURRENT_DATE - 2);

-- Pedido na Loja (Ana comprou Whey)
INSERT INTO pedidos (usuario_id, data_criacao, valor_total) VALUES
    ((SELECT id FROM usuario WHERE email='ana@email.com'), CURRENT_TIMESTAMP, 159.90);

INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal) VALUES
    ((SELECT MAX(id) FROM pedidos), (SELECT id FROM produto WHERE nome LIKE 'Whey%'), 1, 159.90, 159.90);

-- ==============================================================================
-- 9. CHECK-INS
-- ==============================================================================
INSERT INTO checkins (usuario_id, data_hora, tipo) VALUES
    ((SELECT id FROM usuario WHERE email='joao@email.com'), NOW() - INTERVAL '2 HOUR', 'ENTRADA'),
    ((SELECT id FROM usuario WHERE email='joao@email.com'), NOW() - INTERVAL '1 HOUR', 'SAIDA'),
    ((SELECT id FROM usuario WHERE email='ana@email.com'), NOW() - INTERVAL '30 MINUTE', 'ENTRADA');