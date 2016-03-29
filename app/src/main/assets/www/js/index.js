;(function($n2){
    var app = {
        // Application Constructor
        initialize: function() {
            this.bindEvents();
        },
        // Bind Event Listeners
        //
        // Bind any events that are required on startup. Common events are:
        // 'load', 'deviceready', 'offline', and 'online'.
        bindEvents: function() {
            document.addEventListener('deviceready', this.onDeviceReady, false);
        },
        // deviceready Event Handler
        //
        // The scope of 'this' is the event. In order to call the 'receivedEvent'
        // function, we must explicitly call 'app.receivedEvent(...);'
        onDeviceReady: function() {
            app.receivedEvent('deviceready');

            $n2.cordovaPlugin.echo({
                msg: '12345'
                ,onSuccess: function(msg){
                    if( '12345' === msg ){
                        console.log('echo success: '+msg);
                    } else {
                        console.log('echo error: Unexpected message ('+msg+')');
                    };
                }
                ,onError: function(err){
                    console.log('echo error: '+err);
                }
            });

            $n2.cordovaPlugin.getConnectionInfo({
                onSuccess: function(info){
                    console.log('connection name: '+info.name);
                }
            });

            var server = $n2.cordovaCouchbase.getServer();
            var db = server.getDb({
                dbName: 'docs'
            });
            db.getInfo({
                onSuccess: function(info){
                    for(var key in info){
                        var value = info[key];
                        console.log('getInfo '+key+': '+value);
                    };
                }
                ,onError: function(err){
                    console.log('error during db getInfo: '+err);
                }
            });
            db.createDocument({
                data: {
                    test: 'This is just a test'
                }
                ,onSuccess: function(info){
                    console.log('document created: '+info.id+'/'+info.rev);
                    docCreated(info.id);
                }
                ,onError: function(err){
                    console.log('error during document creation: '+err);
                }
            });
            db.listAllDocuments({
				onSuccess: function(docIds){
				    for(var i=0,e=docIds.length; i<e; ++i){
				        var docId = docIds[i];
				        console.log('list all: '+docId);
				    };
				    allDocIdsReported(docIds);
				}
                ,onError: function(err){
                    console.log('error during listing of all documents: '+err);
                }
            });
            db.getAllDocuments({
				onSuccess: function(docs){
				    for(var i=0,e=docs.length; i<e; ++i){
				        var doc = docs[i];
				        console.log('getAllDocuments: '+doc._id);
				    };
				}
                ,onError: function(err){
                    console.log('error during fetching of all documents: '+err);
                }
            });

            function allDocIdsReported(docIds){
                var someDocIds = [];
                for(var i=0,e=docIds.length; i<e && i<5; ++i){
                    var docId = docIds[i];
                    someDocIds.push(docId);
                };
                someDocIds.push('abbccddee');

                console.log('someDocIds.length: '+someDocIds.length);

                db.getDocuments({
                    docIds: someDocIds
                    ,onSuccess: function(docs){
                        for(var i=0,e=docs.length; i<e; ++i){
                            var doc = docs[i];
                            console.log('getDocuments: '+doc._id);
                        };
                    }
                    ,onError: function(err){
                        console.log('error during getting documents: '+err);
                    }
                });
            };

            function docCreated(docId){
                db.getDocumentRevision({
                    docId: docId
                    ,onSuccess: function(rev){
                        console.log('document revision: '+rev);
                    }
                    ,onError: function(err){
                        console.log('error while fetching document revision: '+err);
                    }
                });

                db.getDocument({
                    docId: docId
                    ,onSuccess: function(doc){
                        console.log('document fetched: '+doc._id);
                        docFetched(doc);
                    }
                    ,onError: function(err){
                        console.log('error while fetching document: '+err);
                    }
                });
            };

            function docFetched(doc){
                doc.updated = 'Updated!';
                db.updateDocument({
                    data: doc
                    ,onSuccess: function(info){
                        console.log('document updated: '+info.rev);

                        doc._rev = info.rev;
                        docUpdated(doc);
                    }
                    ,onError: function(err){
                        console.log('error while updating document: '+err);
                    }
                });
            };

            function docUpdated(doc){
                db.deleteDocument({
                    data: doc
                    ,onSuccess: function(info){
                        console.log('document deleted: '+info.id+'/'+info.rev);
                    }
                    ,onError: function(err){
                        console.log('error while deleting document: '+err);
                    }
                });
            };
        },
        // Update DOM on a Received Event
        receivedEvent: function(id) {
            var parentElement = document.getElementById(id);
            var listeningElement = parentElement.querySelector('.listening');
            var receivedElement = parentElement.querySelector('.received');

            listeningElement.setAttribute('style', 'display:none;');
            receivedElement.setAttribute('style', 'display:block;');

            console.log('Received Event: ' + id);
        }
    };

    app.initialize();

})(nunaliit2);
