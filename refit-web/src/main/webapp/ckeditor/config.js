/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.fullPage = true;
};

CKEDITOR.on( 'instanceReady', function( ev )
	    {
	        // Ends self closing tags the HTML4 way, like <br>.
	        ev.editor.dataProcessor.writer.indentationChars = '  ';
	    });