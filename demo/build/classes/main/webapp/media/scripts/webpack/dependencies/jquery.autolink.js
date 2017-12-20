jQuery.fn.autolink = function () {
	return this.each( function(){
		var re = /((http|https|ftp):\/\/[\w?=&.\/!+-;#~%-]+(?![\w\s?&.+!\/;#~%"=-]*>))/g;
		$(this).html( $(this).html().replace(re, '<a href="$1" rel="nofollow" target="_blank">$1</a> ') );
	});
};
