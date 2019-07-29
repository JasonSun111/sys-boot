<div id="{{id}}" class="modal inmodal" tabindex="-1" role="dialog" aria-hidden="true"
	{{if backdrop!=null}}data-backdrop="{{backdrop}}"{{/if}}
	{{if keyboard!=null}}data-keyboard="{{keyboard}}"{{/if}}>
	<div class="modal-dialog" style="{{if width}}max-width:{{width}}px;{{/if}}">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close btn-close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">
					{{if title==null}} {{"cmn.modal.modalTitle" | i18n}} {{else}} {{title | i18n}} {{/if}}
				</h4>
			</div>
			<div class="modal-body" style="{{if bodyHeight}}height:{{bodyHeight}};{{/if}}">
				
			</div>
			<div class="modal-footer">
				{{if showClose==true || !(showClose===false)}}
				<button type="button" class="btn btn-white btn-close" data-dismiss="modal">
					{{if closeText==null}} {{"cmn.modal.close" | i18n}} {{else}} {{closeText | i18n}} {{/if}}
				</button>
				{{/if}}
				{{if showOk==true || !(showOk===false)}}
				<button type="button" class="btn btn-primary btn-ok">
					{{if okText==null}} {{"cmn.modal.ok" | i18n}} {{else}} {{okText | i18n}} {{/if}}
				</button>
				{{/if}}
			</div>
		</div>
	</div>
</div>
