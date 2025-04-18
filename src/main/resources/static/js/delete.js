$(function() {
	$('.delete').on('click', function() {
		if (confirm($(this).parents('.delete-button').find('input[name="deleteContent"]').val()+ "：\n" + $(this).parents('.delete-button').find('input[name="deleteInfo"]').val() + "\n※本当に削除してよろしいですか?")) {
			return true;
		}
		return false;
	});
});