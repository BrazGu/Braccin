$(document).ready(function() {
    $('#validarBtn').click(function() {
        var tipo = $('#tipo').val();
        var documento = $('#documento').val();

        $.ajax({
            url: '/braccin/validar/' + tipo + '/' + documento,
            method: 'GET',
            success: function(response) {
                if (response) {
                    $('#resultado').html('Documento válido!');
                } else {
                    $('#resultado').html('Documento inválido!');
                }
            },
            error: function(xhr, status, error) {
                console.error('Erro ao validar o documento:', xhr.responseText);
                $('#resultado').html('Erro ao validar o documento');
            }
        });
    });
});
