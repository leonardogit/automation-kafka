#language: pt

Funcionalidade: : Enviar e consumir mensagem no topico kafka

  Cenario: Envio mensagem no topico test
    Dado que eu envie a mensagem no topico

  Cenario: Consumo mensagem no topico test
    Dado consumo a mensagem enviada e valido seu conteudo

