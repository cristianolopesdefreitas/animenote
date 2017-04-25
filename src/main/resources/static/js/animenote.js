(function() {
    'user strict';

    function init() {
        $( '#post' ).summernote({
            focus: true,
            height: 300,
            lang: 'pt-BR',
            toolbar: [
                [
                    'style', [
                        'bold',
                        'italic',
                        'underline',
                        'clear'
                    ]
                ],
                [
                    'font', [
                        'strikethrough'
                    ]
                ],
                [
                    'para', [
                        'ul',
                        'ol',
                        'paragraph'
                    ]
                ],
                [
                    'insert', [
                        'picture',
                        'video',
                        'link'
                    ]
                ],
                [
                    'misc', [
                        'fullscreen'
                    ]
                ]
            ]
        });
    }

    $(function() {
        init();
    });
}());
