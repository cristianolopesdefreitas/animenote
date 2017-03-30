(function() {
    'use strict';

    function startSettings() {
        $.validator.setDefaults({
            highlight: function( element ) {
                $( element ).closest( '.form-group' ).addClass( 'has-error' );
            },
            unhighlight: function( element ) {
                $( element ).closest( '.form-group' ).removeClass( 'has-error' );
            },
            errorElement: 'span',
            errorClass: 'help-block',
            errorPlacement: function( error, element ) {
                if ( element.parent( '.input-group' ).length ) {
                    error.insertAfter( element.parent() );
                } else if ( element.closest( '.radio' ).length ) {
                    error.insertAfter( element.closest( '.form-group' ).find( '.radio' ).last() );
                } else {
                    error.insertAfter( element );
                }
            }
        });
    }

    startSettings();

}());
