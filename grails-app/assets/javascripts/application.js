// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap
//= require_tree .
//= require_self

if (typeof jQuery !== 'undefined') {
    (function($) {
        $('#spinner').ajaxStart(function() {
            $(this).fadeIn();
        }).ajaxStop(function() {
            $(this).fadeOut();
        });
    })(jQuery);
}

function setCleaning(cleaning) {
    if(cleaning) {
        document.getElementById('cleanButton').style.visibility='visible';
        document.getElementById('cleanButton').style.height=35;
        document.getElementById('cleaning').style.visibility='hidden';
        document.getElementById('cleaning').style.height=0;
    } else {
        document.getElementById('cleanButton').style.visibility='hidden';
        document.getElementById('cleanButton').style.height=0;
        document.getElementById('cleaning').style.visibility='visible';
        document.getElementById('cleaning').style.height=35;
    }

}
