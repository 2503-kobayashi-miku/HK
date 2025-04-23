$(function() {
    $('.tweet-box').each(function() {
        $(this).closest('.form-area').find('.charCount').text($(this).val().length + "/" + $(this).parents('.form-area').find('input[name="maxLength"]').val() + "文字")
    });
    $('.tweet-box').on('input', function() {
        $(this).closest('.form-area').find('.charCount').text($(this).val().length + "/" + $(this).parents('.form-area').find('input[name="maxLength"]').val() + "文字")
    });
});