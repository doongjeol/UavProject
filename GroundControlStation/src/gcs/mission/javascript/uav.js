
//드론
function UAV() {
	this.frameIcon1 = {
			path:"M-11.337985123093134,2.9154191295533707 c-0.24099002853730855,0.39272809577869117 -0.5315212220687566,0.7622915603813405 -0.8710095982957888,1.1021692581407432 c-2.3660042825983147,2.3656149610659445 -6.213571656632373,2.3656149610659445 -8.579575939230688,0 c-2.3625003888069807,-2.3629870407224445 -2.365906952215222,-6.213571656632373 0,-8.579478608847598 c2.3660042825983147,-2.3660042825983147 6.216588898508245,-2.3625977191900733 8.579575939230688,0 c0.37258070647852076,0.3729700280108907 0.6861792008029176,0.785456191557382 0.9436180640828773,1.2207176647475446 c0.7523638613058944,0.037277536724470706 1.5185486370109387,-0.09236653355488943 2.2675059349085904,-0.3774472256331511 c0.041073421665082516,-0.014891548613169702 0.08282815601181338,-0.02588988190263496 0.1236095865276181,-0.041462743197452995 c0,0 -0.00038932153237045087,-0.0006813126816482897 -0.0007786430647409019,-0.001459955746389191 c0.004963849537723253,-0.001946607661852257 0.010317020607816932,-0.0030172418758709955 0.014794218230077127,-0.004963849537723253 c-0.4055757063469171,-0.8877504241877179 -0.9698972675178827,-1.7162266450720343 -1.6990964976477347,-2.4454258752018863 c-3.2731234530214612,-3.2731234530214612 -8.602643240023637,-3.276530016429704 -11.879173256453344,0 c-3.2731234530214612,3.273512774553833 -3.2731234530214612,8.60614713381497 0,11.879173256453344 c3.2769193379620742,3.2769193379620742 8.60614713381497,3.273512774553833 11.879173256453344,0 c0.6775167967076751,-0.6775167967076751 1.2136125467817844,-1.4450641977760161 1.6123751263122168,-2.262542085370867 c0.012944940951317487,-0.02550056037026452 0.028517802246135537,-0.0502224776757882 0.041462743197452995,-0.07611235957842302 l-0.0010706342140187398,-0.00038932153237045087 c0.0022385988111301006,-0.004477197622260192 0.004866519154630641,-0.009149056010705604 0.007202448348853344,-0.013626253632965803 C-9.658257371680829,2.991434158748689 -10.513304787149416,2.849331799433493 -11.337985123093134,2.9154191295533707 z M-3.511259697083787,-9.356871544211035 c0.02958843646015425,0.014794218230077127 0.058203569089382325,0.03270300871911784 0.08818132708190704,0.047107905416824525 l0.00038932153237045087,-0.001167964597111353 c0.004574528005352812,0.0023359291942227096 0.009149056010705604,0.004963849537723253 0.014112905548428844,0.007202448348853344 c0.3197303084592326,-0.7885707638163456 0.44869306605694387,-1.6302839168012575 0.36304232893544475,-2.4548669223618695 c-0.39272809577869117,-0.24108735892040128 -0.7622915603813405,-0.5311319005363858 -1.0991520162648718,-0.8679923564199175 c-2.3625977191900733,-2.3625977191900733 -2.3660042825983147,-6.213571656632373 0,-8.579575939230688 c2.3660042825983147,-2.3656149610659445 6.2168808896575225,-2.3625977191900733 8.579478608847598,0 c2.3660042825983147,2.3660042825983147 2.3660042825983147,6.213571656632373 0,8.579575939230688 c-0.3729700280108907,0.3729700280108907 -0.7850668700250115,0.6865685223352879 -1.2207176647475446,0.9440073856152476 c-0.04302002932693479,0.4980395702848984 -0.013334262483687936,1.0028922673862781 0.09236653355488943,1.491101468978822 c0.06277809709473513,0.307174689040285 0.16156843593373704,0.6170746288071628 0.2871246301232067,0.934177016922894 c0.9041019285472764,-0.403045116386508 1.7488323234080596,-0.9767103943343655 2.4914631464046915,-1.7192438869479052 c3.2731234530214612,-3.273512774553833 3.276530016429704,-8.60225391849127 0,-11.879173256453344 c-3.273512774553833,-3.273512774553833 -8.60614713381497,-3.273512774553833 -11.879270586836435,0 c-3.2768220075789807,3.2769193379620742 -3.273512774553833,8.605757812282603 0,11.879173256453344 C-5.1103005609123215,-10.29260584726341 -4.336621345709148,-9.755342132592183 -3.511259697083787,-9.356871544211035 z M13.136219795504665,0.7456505925139972 l-6.34960517922641,1.264882519877658 c-0.017081411338573466,0.003275887391925869 -0.0333828495566641,0.006551774783851736 -0.05054225820728589,0.010607635364331383 c-2.3700263238990473,0.49309904980369684 -4.222462485045706,2.3447553994303596 -4.714703521382405,4.714703932851006 c-0.004055860226510579,0.01708141282932778 -0.0073317473325383775,0.03338285247010172 -0.010607634438566178,0.05054226261828482 l-1.264882409486963,6.349605733378575 c-0.1833716806255075,0.9168584831449637 -1.4931025445402317,0.9168584831449637 -1.6756162547332076,0 l-1.264882409486963,-6.349605733378575 c-0.003275887106027785,-0.01708141282932778 -0.006551774212055576,-0.03424082297751081 -0.010607634438566178,-0.05054226261828482 c-0.17034612951344474,-0.8199078158077289 -0.5036286438957472,-1.5778077631254317 -0.9649827446613245,-2.238757043105901 v-0.000779973188553781 c-0.8720099486997768,-1.249361053425438 -2.199680194385603,-2.1531939843215535 -3.749798774033127,-2.4751669165565504 c-0.017081411338573466,-0.004055860580479639 -0.0333828495566641,-0.00733174797240551 -0.05054225820728589,-0.010607635364331383 l-6.34960517922641,-1.264882519877658 c-0.9168584031275366,-0.1833716966289929 -0.9168584031275366,-1.4922447043410838 0,-1.675616400970076 l6.34960517922641,-1.264882519877658 c0.017081411338573466,-0.003275887391925869 0.0333828495566641,-0.006551774783851736 0.05054225820728589,-0.010607635364331383 c2.3691683534665167,-0.49309904980369684 4.220824541492693,-2.3447553994303596 4.714703521382405,-4.713923959662452 c0.003275887106027785,-0.01708141282932778 0.0073317473325383775,-0.03424082297751081 0.010607634438566178,-0.05132223580683852 l1.264882409486963,-6.349605733378575 c0.18259170750502493,-0.9160785099564085 1.492244574107701,-0.9160785099564085 1.6756162547332076,0 l0.6413718969730121,3.2200413116254065 l0.6234325152019055,3.1295644217531677 c0.003275887106027785,0.01708141282932778 0.006551774212055576,0.03424082297751081 0.010607634438566178,0.05132223580683852 c0.4930990067692288,2.369168560232093 2.3455351679158882,4.220824909858755 4.714703521382405,4.713923959662452 c0.017081411338573466,0.004055860580479639 0.0333828495566641,0.00733174797240551 0.05054225820728589,0.010607635364331383 l6.34960517922641,1.264882519877658 C14.052298225511713,-0.7466721091459441 14.052298225511713,0.5622008985661505 13.136219795504665,0.7456505925139972 z M3.723113017423909,8.649541319071538 c-0.006813126816482879,-0.0037958849406119015 -0.012944940951317487,-0.008759734478335153 -0.01975806776780041,-0.012555619418947027 l-0.00038932153237045087,0.001167964597111353 c-0.004574528005352812,-0.0023359291942227096 -0.009149056010705604,-0.005353171070093712 -0.01372358401605839,-0.007591769881223795 c-0.3532119602430912,0.7622915603813405 -0.5181869595850686,1.5873612178574157 -0.48509462933358094,2.4058124092831927 c0.34980539683484946,0.2307703383125844 0.6797553955188051,0.49463300687665623 0.9865407630267198,0.8014183743845709 c2.3625977191900733,2.3625977191900733 2.3660042825983147,6.213571656632373 0,8.579575939230688 c-2.365906952215222,2.365906952215222 -6.2168808896575225,2.3625003888069807 -8.579478608847598,0 c-2.3660042825983147,-2.3660042825983147 -2.3660042825983147,-6.213571656632373 0,-8.579575939230688 c0.4090796001382507,-0.4090796001382507 0.8645857930116763,-0.7455507344894113 1.3496804223452563,-1.0132092879940953 c-0.009538377543076054,-0.7634595249784519 -0.18590103170689026,-1.5398639909082208 -0.5231508091227917,-2.2858040469300023 c-0.0052558406870010805,-0.012944940951317487 -0.009538377543076054,-0.026279203435005408 -0.015183539762447552,-0.03951613553560072 l-0.00038932153237045087,0.00038932153237045087 c-0.0022385988111301006,-0.004477197622260192 -0.0037958849406119015,-0.009538377543076054 -0.005645162219371541,-0.014404896697706684 c-0.8908649964466817,0.40265579485413844 -1.7226504503561473,0.9702865890502526 -2.4548669223618695,1.7028923825883462 c-3.273512774553833,3.2731234530214612 -3.2769193379620742,8.60225391849127 0,11.879173256453344 c3.2731234530214612,3.2731234530214612 8.605757812282603,3.2731234530214612 11.879173256453344,0 c3.276530016429704,-3.2769193379620742 3.2731234530214612,-8.60614713381497 0,-11.879173256453344 C5.203605474645624,9.551015327275312 4.485793899337617,9.040030816039089 3.723113017423909,8.649541319071538 z M22.484614992738955,-6.456036806518803 c-3.276530016429704,-3.276530016429704 -8.605757812282603,-3.2731234530214612 -11.879173256453344,0 c-0.7619022388489707,0.7619995692320622 -1.3485124577481449,1.6402116158767046 -1.753796172945783,2.5732206682024863 c-0.0037958849406119015,0.007591769881223795 -0.007981091413594245,0.015280870145540203 -0.011484985204928315,0.022775309643671367 v0.00038932153237045087 c-0.001849277278759646,0.004574528005352812 -0.004477197622260192,0.009149056010705604 -0.006813126816482879,0.014112905548428844 c0.6106508235230506,0.28011684254053887 1.2642243459899425,0.4713710453175224 1.9405731781005058,0.5212042014609394 c0.13197999947358266,0.006423805284112443 0.2604561051558314,0.009538377543076054 0.3923387742463217,0.009927699075446493 c0.2741796891718895,-0.5379450273528692 0.6402392599832047,-1.042895054837342 1.089224317189426,-1.4914907905111927 c2.3660042825983147,-2.3660042825983147 6.213182335100005,-2.3660042825983147 8.579186617698321,0 c2.3625977191900733,2.3625977191900733 2.3660042825983147,6.213474326249282 0,8.579478608847598 c-2.3656149610659445,2.3656149610659445 -6.216588898508245,2.3625977191900733 -8.579186617698321,0 c-0.26415465971335045,-0.26415465971335045 -0.4984288918172683,-0.5444661630200739 -0.7029200266948471,-0.8417131529849119 c-0.747108020618893,-0.05440768414877049 -1.5096915721495114,0.05927420330340115 -2.2567995927684037,0.3265434352757149 c-0.06696330356771749,0.022775309643671367 -0.1346079198170832,0.041073421665082516 -0.20069524993696755,0.06579533897060605 c0.00038932153237045087,0.0006813126816482897 0.0007786430647409019,0.001459955746389191 0.0007786430647409019,0.001459955746389191 c-0.004574528005352812,0.00262792034350054 -0.009927699075446493,0.0037958849406119015 -0.014891548613169702,0.005645162219371541 c0.3863042904945793,0.7554784335648577 0.8941742294718299,1.4615130325186674 1.5245831207626797,2.0919219238095184 c3.273512774553833,3.273512774553833 8.602643240023637,3.2768220075789807 11.879173256453344,0 C25.758127767292795,2.1501103272961615 25.758127767292795,-3.182524031964988 22.484614992738955,-6.456036806518803 z",
			fillColor: "#ffffff",
		    fillOpacity:1,
			strokeColor: "#000000",
			strokeWeight: 0.5
		};	
		
		this.frameIcon2 = {
			path:"M-24.073774881675437,-25.27312956299825 L-10.518265214148316,-25.27312956299825 L-15.036722338796345,-20.754671069107005 L-19.555316387784263,-20.754671069107005 L-19.555316387784263,-16.236077476533637 L-24.073774881675437,-11.71761943905677 L-24.073774881675437,-25.27312956299825 z",
			strokeColor: '#000000',
			strokeWeight: 0.5,
			fillColor: "#eb1a1a",
		    fillOpacity:1
		};
		
	this.frameMarker1 = new google.maps.Marker({
		icon: this.frameIcon1,
		map: map.googlemap,
		optimized: false,
		zIndex: google.maps.Marker.MAX_ZINDEX + 1
	});
	
	this.frameMarker2 = new google.maps.Marker({
		icon: this.frameIcon2,
		map: map.googlemap,
		optimized: false,
		zIndex: google.maps.Marker.MAX_ZINDEX + 1
	});
	//---------------------------------------------
	this.drawUav = function() {
		try {
			this.frameIcon1.rotation = 45 + this.heading;
			this.frameMarker1.setIcon(this.frameIcon1);
			
			this.frameIcon2.rotation = 45 + this.heading;
			this.frameMarker2.setIcon(this.frameIcon2);
			
			this.frameMarker1.setPosition(this.currLocation);	
			this.frameMarker2.setPosition(this.currLocation);
		} catch(err) {
			console.log(">> [uav.drawUav()] " + err);
		}
	};	
	//----------------------------------------------
	this.currLocation = null;
	this.heading = 0;	
	this.setCurrLocation = function(latitude, longitude, heading) {
		try {
			this.currLocation = {lat:latitude, lng:longitude};
			this.heading = heading;
		} catch(err) {
			console.log(">> [uav.setCurrLocation()] " + err);
		}	
	};
	//---------------------------------------------
	this.headingLine = null;
	this.drawHeadingLine = function() {
		try {
			if(this.headingLine != null) {
				this.headingLine.setMap(null);
			}
			
			var endheadingLatLng = google.maps.geometry.spherical.computeOffset(
				new google.maps.LatLng(map.uav.currLocation), 
				2000, 
				this.heading
			);
			
			this.headingLine = new google.maps.Polyline({
				path: [
					this.currLocation,
					endheadingLatLng.toJSON()
				],
				strokeColor: '#FF0000',
				strokeOpacity: 1.0,
				strokeWeight: 2,
				map: map.googlemap
		    });
		} catch(err) {
			console.log(">> [uav.drawHeadingLine()] " + err);
		}
	};	
	//----------------------------------------------
	this.homeLocation = null;
	this.homeMarker = null;
	//---------------------------------------------
	this.goto = false;
	this.gotoMarker = null;
	this.gotoLocation = null;
	//---------------------------------------------
	this.mission = false;
	this.missionMarkers = [];
	this.missionPolylines = [];	
	this.nextLocation = null;	
	this.nextMarker = null;
	this.roiIndex = 0;
	//---------------------------------------------
	this.rtl = false;	
	//---------------------------------------------
	this.destLocation = null;
	this.destLine = null;	
	//---------------------------------------------
	this.drawDestLine = function() {
		try {
			if(this.goto == true) {
				this.destLocation = this.gotoMarker.getPosition().toJSON();
			} else if(this.mission == true) {
				this.destLocation = this.nextLocation;
			} else if(this.rtl == true) {
				this.destLocation = this.homeLocation;
			} else {
				this.destLocation = this.currLocation;
			}
			
			var bearing = google.maps.geometry.spherical.computeHeading(
				new google.maps.LatLng(this.currLocation), 
				new google.maps.LatLng(this.destLocation)
			);
			if(bearing<0) {
				bearing += 360;
			}
			
			var endbearingLatLng = google.maps.geometry.spherical.computeOffset(
				new google.maps.LatLng(this.currLocation), 
				2000, 
				bearing
			);
			
			if(this.destLine != null)  this.destLine.setMap(null);
			this.destLine = new google.maps.Polyline({
				path: [
					this.currLocation,
					endbearingLatLng.toJSON()
				],
				strokeColor: '#FFFFFF',
				//strokeColor: '#FF9900',
				strokeOpacity: 1.0,
				strokeWeight: 2,
				map: map.googlemap
		    });
		} catch(err) {
			console.log(">> [uav.drawDestLine()] " + err);
		}
	};		
	//---------------------------------------------
	this.setUavColor = function(color1, color2) {
		try {
			this.frameIcon1.strokeColor = color1;
			this.frameIcon2.strokeColor = color2;
			this.frameMarker1.setIcon(this.frameIcon1);
			this.frameMarker2.setIcon(this.frameIcon2);
		} catch(err) {
			console.log(">> [uav.setUavColor()] " + err);
		}
	};
	//---------------------------------------------
	this.setHomeLocation = function(latitude, longitude) {
		try {
			if(this.homeLocation == null || this.homeLocation.lat!=latitude || this.homeLocation.lng!=longitude) {
				this.homeLocation = {lat:latitude, lng:longitude};
				if(this.homeMarker != null) {
					this.homeMarker.setMap(null);
				}
				if(this.gotoMarker != null) {
					this.gotoMarker.setMap(null);
				}
				var icon = {
						url:'../../images/home_marker.png',
						scaledSize: new google.maps.Size(45, 65)
				}
				this.homeMarker = new google.maps.Marker({
					icon: icon,
					map: map.googlemap,
					position: this.homeLocation,
					label: {color:"#FF3643", fontSize: '12px', fontWeight: 'bold', text:"H"},
					optimized: false
				});

				map.googlemap.setCenter(map.uav.currLocation);
				map.googlemap.setZoom(18);
				jsproxy.setZoomSliderValue(18);
			}
		} catch(err) {
			console.log(">> [uav.setHomeLocation()] " + err);
		}
	};	
	//---------------------------------------------
	this.gotoStop = function() {
		try {
			this.goto = false;
			if(this.gotoMarker != null) {
				this.gotoMarker.setMap(null);
			}
		} catch(err) {
			console.log(">> [uav.gotoStart()] " + err);
		}
	};
	//---------------------------------------------
	this.alt = 0;
	//---------------------------------------------
	this.makeMissionMark = function(kind, lat, lng, index, alt, modifiedKind, jump, repeat) {
		try {
			var marker = null;
			
			if(kind == "takeoff") {
				marker = new google.maps.Marker({
					position: map.uav.currLocation
				});
				marker.kind = kind;
				marker.index = 0;
				marker.jump = 0;
				marker.repeat = 0;
				marker.alt = alt;
				this.missionMarkers.splice(marker.index,0,marker);
			} 
			
			else if(kind == "waypoint") {
				var icon = {
						url:'../../images/make_marker.png',
						scaledSize: new google.maps.Size(40, 56)
				}
				
				marker = new google.maps.Marker({
					map: map.googlemap,
					position: {lat:lat, lng:lng},
					icon: icon,
					draggable: true,
					optimized: false
				});
				marker.kind = kind;
				marker.index = this.missionMarkers.length;
				marker.jump = 0;
				marker.repeat = 0;
				marker.alt = alt;
				this.missionMarkers.push(marker);
				var self = this;
				marker.addListener('drag', function() {
					self.drawMissionPath();
					jsproxy.java.getMission();
				});
			} 
			
			else if(kind == "modifiedWayPoint") {
				console.log("modifiedwaypoint");
	            if(modifiedKind == "waypoint") {
	            	console.log("waypoint");
	               var icon = {
	                     url:'../../images/make_marker.png',
	                     scaledSize: new google.maps.Size(40, 56)
	               }
	               
	               marker = new google.maps.Marker({
	                  map: map.googlemap,
	                  position: {lat:lat, lng:lng},
	                  icon: icon,
	                  draggable: true,
	                  optimized: false
	               });
	               marker.kind = "waypoint";
	               marker.index = index;
	               marker.jump = 0;
	               marker.repeat = 0;
	               marker.alt = alt;
	               this.missionMarkers.splice(marker.index, 0, marker);
	               var self = this;
	               console.log(marker);
	               marker.addListener('drag', function() {
	                  self.drawMissionPath();
	                  jsproxy.java.getMission();
	               });
	            }
	            else if(modifiedKind == "takeoff") {
	               this.makeMissionMark("takeoff", lat, lng, index, alt);
	            }
	            else if(modifiedKind == "rtl") {
	               this.makeMissionMark("rtl", lat, lng);
	            }
	            else if(modifiedKind == "roi") {
	               this.makeMissionMark("roi", lat, lng, index);
	            }
	            else if(modifiedKind == "jump") {
	               this.makeMissionMark("moveJump", lat, lng, index, 0, "", jump, repeat);
	            }
	            else if(modifiedKind == "land") {
		           this.makeMissionMark("moveLand", lat, lng, index);
		        }
			}
			
			else if(kind == "rtl") {
				marker = new google.maps.Marker({
					position: map.uav.homeLocation
				});
				marker.kind = kind;
				marker.index = this.missionMarkers.length;
				marker.jump = 0;
	            marker.repeat = 0;
	            marker.alt = 0;
				this.missionMarkers.push(marker);
			}
			
			else if(kind == "roi") {
				var icon = {
						url:'../../images/roi_marker.png',
						scaledSize: new google.maps.Size(40, 56)
				}
				marker = new google.maps.Marker({
					map: map.googlemap,
					icon: icon,
					position: {lat:lat, lng:lng},
					draggable: true,
					optimized: false
				});
				marker.kind = kind;
				marker.jump = 0;
	            marker.repeat = 0;
	            marker.alt = 0;
				if(index) {
					marker.index = index;
					this.missionMarkers.splice(marker.index,0,marker);
				} else {
					marker.index = this.missionMarkers.length;
					this.missionMarkers.push(marker);
				}
				marker.addListener('drag', function() {
                  jsproxy.java.getMission();
               });
			}
			else if(kind == "land") {
				var icon = {
						url:'../../images/make_marker.png',
						scaledSize: new google.maps.Size(40, 56)
				}
				marker = new google.maps.Marker({
					map: map.googlemap,
					position: {lat:lat, lng:lng},
					icon: icon,
					draggable: true,
					optimized: false
				});
				marker.kind = kind;
				marker.index = index;
				marker.jump = 0;
	            marker.repeat = 0;
	            marker.alt = 0;
	            
	            var self = this;
	            
				this.missionMarkers.push(marker);
				
				for(var i=0; i<this.missionMarkers.length; i++) {
					console.log("no: " + i);
					console.log("kind: " + this.missionMarkers[i].kind);
				}
				
				marker.addListener('drag', function() {
	               self.drawMissionPath();
	               jsproxy.java.getMission();
	            });
			}
			else if(kind == "jump") {
				marker = new google.maps.Marker({
					position: {lat:lat, lng:lng},
					optimized: false
				});
				marker.kind = kind;
				marker.jump = jump;
				marker.repeat = repeat;
				marker.alt = 0;
				
				if(index) {
					marker.index = index;
					this.missionMarkers.splice(marker.index,0,marker);
				} else {
					marker.index = this.missionMarkers.length;
					this.missionMarkers.push(marker);
				}
				
				var self = this;
				
				marker.addListener('drag', function() {
		               self.drawMissionPath();
		               jsproxy.java.getMission();
		        });
			}
			else if(kind == "moveJump") {
				console.log("here is moveJump");
				marker = new google.maps.Marker({
					position: {lat:lat, lng:lng},
					optimized: false
				});
				marker.kind = "jump";
				marker.index = index;
				marker.jump = jump;
				marker.repeat = repeat;
				marker.alt = 0;
				this.missionMarkers.splice(marker.index, 0, marker);
				console.log("ens moveJump");
			}
			
			else if(kind == "moveLand") {
				var icon = {
						url:'../../images/make_marker.png',
						scaledSize: new google.maps.Size(40, 56)
				}
				marker = new google.maps.Marker({
					map: map.googlemap,
					position: {lat:lat, lng:lng},
					icon: icon,
					draggable: true,
					optimized: false
				});
				marker.kind = "land";
				marker.index = index;
				marker.jump = 0;
	            marker.repeat = 0;
	            marker.alt = 0;
	            var self = this;
	            
				this.missionMarkers.splice(index,0,marker);
				
				for(var i=0; i<this.missionMarkers.length; i++) {
					console.log("no: " + i);
					console.log("kind: " + this.missionMarkers[i].kind);
				}
				
				marker.addListener('drag', function() {
	               self.drawMissionPath();
	               jsproxy.java.getMission();
	            });
			}
			
			//마커안 숫자
			for(var i=0; i<this.missionMarkers.length; i++) {
				this.missionMarkers[i].index = i;
				if(this.missionMarkers[i].kind != "takeoff" && this.missionMarkers[i].kind != "rtl" && this.missionMarkers[i].kind != "jump") {
					this.missionMarkers[i].setLabel({color: '#3867FF', fontSize: '12px', fontWeight: 'bold', text: String(i+1)});
				}
			}
			
			this.drawMissionPath();
			jsproxy.java.getMission();
		} catch(err) {
			console.log(">> [uav.makeMissionMark()] " + err);
		}
	};
	//---------------------------------------------
	this.gotoStart = function(location) {
		try {
			this.goto = true;
			var icon = {
					url:'../../images/goto_marker.png',
					scaledSize: new google.maps.Size(40, 56)
			}
			if(this.gotoMarker != null) {
				this.gotoMarker.setMap(null);
			} 
			this.gotoMarker = new google.maps.Marker({
				map: map.googlemap,
				position: location,
				optimized: false,
				icon:icon,
				label: {color:"#7a7a7a", fontSize: '12px', fontWeight: 'bold', text:"G"},
			});
			this.missionStop();
			this.rtlStop();
			jsproxy.gotoStart(location);
		} catch(err) {
			console.log(">> [uav.gotoStart()] " + err);
		}
	};
	//---------------------------------------------
	this.missionStart = function() {
		this.mission = true;
		this.gotoStop();
		this.rtlStop();
		
/*		while(true) {
			var point = new google.maps.Point()
		}
		
		
		var icon = {
				url:'make_marker.png',
				scaledSize: new google.maps.Size(100, 80),
				position: {lat:Network.getUav().latitude, lng:Network.getUav().longitude},
		}
		marker = new google.maps.Marker({
			map: map.googlemap,
		});*/
		
	};
	//---------------------------------------------
	this.missionStop = function() {
		this.mission = false;
	};
	//---------------------------------------------
	this.rtlStart = function() {
		this.rtl = true;
		this.missionStop();
		this.gotoStop();
	};
	//---------------------------------------------
	this.rtlStop = function() {
		this.rtl = false;
	};	
	//---------------------------------------------
	this.removeMissionMark = function(index) {
		try {
			this.missionMarkers[index].setMap(null);
			this.missionMarkers.splice(index,1);
			for(var i=0; i<this.missionMarkers.length; i++) {
				this.missionMarkers[i].index = i;
				if(this.missionMarkers[i].kind != "takeoff" && this.missionMarkers[i].kind != "rtl" && this.missionMarkers[i].kind != "jump") {
					this.missionMarkers[i].setLabel({color: '#3867FF', fontSize: '12px', fontWeight: 'bold', text: String(i+1)});
				}
			}
			this.drawMissionPath();
		} catch(err) {
			console.log(">> [uav.gotoStart()] " + err);
		}
	};
	//---------------------------------------------
	this.clearMission = function() {
		try {
			for(var i=0; i<map.uav.missionMarkers.length; i++) {
				map.uav.missionMarkers[i].setMap(null);
			}
			map.uav.missionMarkers = [];
			for(var i=0; i<map.uav.missionPolylines.length; i++) {
				map.uav.missionPolylines[i].setMap(null);
			}
			map.uav.missionPolylines = [];	
		} catch(err) {
			console.log(">> [uav.clearMission()] " + err);
		}
	};
	//---------------------------------------------
	this.setMission = function(missionArr) {
		try {
			this.clearMission();
			for(var i=0; i<missionArr.length; i++) {
				this.makeMissionMark(missionArr[i].kind, missionArr[i].lat, missionArr[i].lng, 0, missionArr[i].alt, "", missionArr[i].jump, missionArr[i].repeat);
			}
		} catch(err) {
			console.log(">> [uav.setMission()] " + err);
		}
	};
	//---------------------------------------------
	this.setNextWaypointNo = function(nextWaypointNo) {
		try {
			if(this.mission == true) {
				if(1<=nextWaypointNo && (nextWaypointNo)<=this.missionMarkers.length) {
					this.nextLocation = this.missionMarkers[nextWaypointNo-1].getPosition().toJSON();
					this.nextMarker = this.missionMarkers[nextWaypointNo-1];
				}
			}
		} catch(err) {
			console.log(">> [uav.setNextWaypointNo()] " + err);
		}
	};
	//---------------------------------------------
	this.drawMissionPath = function() {
		try {
			for(var i=0; i<this.missionPolylines.length; i++) {
				this.missionPolylines[i].setMap(null);
			}
			this.missionPolylines = [];	
			
			var nowMarker = this.missionMarkers[0];
			var prevMarker = this.missionMarkers[0];
			var nextMarker = null;
			for(var i=1; i<this.missionMarkers.length; i++) {			 
				var polyline = new google.maps.Polyline({
					strokeColor: '#cccccc',
					//strokeColor: '#1ea4ff',
					strokeOpacity: 0.8,
					strokeWeight: 5,
					map: map.googlemap
				});
				
				nextMarker = this.missionMarkers[i];
				
				if(nextMarker.kind == "roi") {
					continue;
				} else {
					if(nowMarker.kind == "jump") {
						polyline.setPath([prevMarker.getPosition(), nextMarker.getPosition()]);
					} else {
						polyline.setPath([nowMarker.getPosition(), nextMarker.getPosition()]);
					}
				}
				
				this.missionPolylines.push(polyline);
				prevMarker = nowMarker;
				nowMarker = nextMarker;
				
				
			}
		} catch(err) {
			console.log(">> [uav.drawMissionPath()] " + err);
		}
	};
	//Fence 관련 속성 및 메소드-----------------------------------------------------------
	this.fenceMarkers = [];
	this.makeFenceMark = function(lat, lng, kind, index) {
		try {
			if(kind == "modifiedFencePoint") {
				var image = {
		          url: '../../images/fence_marker.png',
		          scaledSize: new google.maps.Size(40, 30)
		        };
				var marker = new google.maps.Marker({
					map: map.googlemap,
					position: {lat:lat, lng:lng},
					icon: image,
					draggable: true,
					optimized: false
				});
				
				marker.index = index;
				this.fenceMarkers.splice(marker.index, 0, marker);
				
				var self = this;
				marker.addListener('drag', function() {
					self.drawFencePolygon();
					jsproxy.java.getFence();
				});
			}
			else {
				var image = {
		          url: '../../images/fence_marker.png',
		          scaledSize: new google.maps.Size(40, 30)
		        };
				var marker = new google.maps.Marker({
					map: map.googlemap,
					position: {lat:lat, lng:lng},
					icon: image,
					draggable: true,
					optimized: false
				});
				
				var self = this;
				marker.addListener('drag', function() {
					self.drawFencePolygon();
					jsproxy.java.getFence();
				});
				this.fenceMarkers.push(marker);
			}
			
			for(var i=0; i<this.fenceMarkers.length; i++) {
				this.fenceMarkers[i].index = i;
			}
			this.drawFencePolygon();
			jsproxy.java.getFence();
		} catch(err) {
			console.log(">> [uav.makeFenceMark()] " + err);
		}
	};
	this.setFence = function(fencePoints) {
		try {
			this.fenceClear();
			for(var i=0; i<fencePoints.length; i++) {
				this.makeFenceMark(fencePoints[i].lat, fencePoints[i].lng);
			}
		} catch(err) {
			console.log(">> [uav.setFence()] " + err);
		}
	};	
	this.removeFenceMark = function(index) {
		try {
			this.fenceMarkers[index].setMap(null);
			this.fenceMarkers.splice(index,1);
			for(var i=0; i<this.fenceMarkers.length; i++) {
				this.fenceMarkers[i].index = i;
			}
			this.drawFencePolygon();
		} catch(err) {
			console.log(">> [uav.removeFenceMark()] " + err);
		}
	};
	this.fenceClear = function() {
		try {
			if(this.fencePolygon != null) {
				this.fencePolygon.setMap(null);
			}
			for(var i=0; i<this.fenceMarkers.length; i++) {
				this.fenceMarkers[i].setMap(null);
			}
			this.fenceMarkers = [];
			jsproxy.java.getFence();
		} catch(err) {
			console.log(">> [uav.clearFence()] " + err);
		}
	};
	this.fencePolygon = null;
	this.drawFencePolygon = function() {
		try {
			var coords = [];
			for(var i=0; i<this.fenceMarkers.length; i++) {
				coords.push(this.fenceMarkers[i].getPosition());
			}
			if(this.fencePolygon != null) {
				this.fencePolygon.setMap(null);
			}
			this.fencePolygon = new google.maps.Polygon({
				map: map.googlemap,
				paths: coords,
				strokeColor: '#FF0000',
				strokeOpacity: 0.8,
				strokeWeight: 2,
				//fillColor: '#00ff00',
		        fillOpacity: 0.15
			});
			
			this.fencePolygon.addListener('click', function(e) {
				if(map.gotoMake == true) {
					var clickLocation = {lat:e.latLng.lat(), lng:e.latLng.lng()};
					map.uav.gotoStart(clickLocation);
				} else if(map.missionMake == true) {
					map.uav.makeMissionMark("waypoint", e.latLng.lat(), e.latLng.lng());
				} else if(map.roiMake == true) {
					map.uav.makeMissionMark("roi", e.latLng.lat(), e.latLng.lng(), map.uav.roiIndex);
					jsproxy.addROI({lat:e.latLng.lat(), lng:e.latLng.lng()});
					map.roiMake = false;
				}
			});
		} catch(err) {
			console.log(">> [uav.drawFencePolygon()] " + err);
		}
	};
//	NoFlyZone -----------------------------------------------------------------------------------------
	this.noflyZoneMarkers = [];
	this.noflyZoneCircle = [];
	this.makeNoFlyZoneMark = function(lat, lng) {
		try {
			var image = {
	          url: '../../images/noflyzone_marker.png',
	          scaledSize: new google.maps.Size(30, 42)
	        };
			var marker = new google.maps.Marker({
				map: map.googlemap,
				position: {lat:lat, lng:lng},
				icon: image,
				draggable: false,
				optimized: false
			});
			
			if(this.noflyZoneMarkers.length != 0) {
				this.noflyZoneMarkers[0].setMap(null);
				this.noflyZoneMarkers.splice(0, 1);
				this.noflyZoneMarkers.push(marker);
			} else {
				this.noflyZoneMarkers.push(marker);
			}
			
			var self = this;
			for(var i=0; i<this.noflyZoneMarkers.length; i++) {
				this.noflyZoneMarkers[i].index = i;
			}

			jsproxy.java.getNoFlyZone();
		} catch(err) {
			console.log(">> [uav.makeNoFlyZoneMark()] " + err);
		}
	},
	this.addNoFlyZone = function(latitude, longitude, radius) {
		try {
			var circle = null;
			var center = new google.maps.LatLng(latitude, longitude);
			
			circle = new google.maps.Circle({
				center: center,
				map: map.googlemap,
				radius: radius,
				strokeColor:"#0000FF",
				strokeOpacity:0.8,
				strokeWeight:2,
				fillColor:"#0000FF",
				fillOpacity:0.4
			});
			this.noflyZoneCircle.push(circle);
			
			var self = this;
			for(var i=0; i<this.noflyZoneCircle.length; i++) {
				this.noflyZoneCircle[i].index = i;
			}
			
		} catch(err) {
			console.log(">> [uav.addNoFlyZone()] " + err);
		}
	};
	this.removeNoFlyZone = function(index, isClicked_imgNoFlyZoneView) {
		try {
			if(!isClicked_imgNoFlyZoneView) {
				if(this.noflyZoneMarkers.length != 0) {
					this.noflyZoneMarkers[0].setMap(null);
					this.noflyZoneMarkers.splice(0,1);
					for(var i=0; i<this.noflyZoneMarkers.length; i++) {
						this.noflyZoneMarkers[i].index = i;
					}
				}
			} else {
				if(this.noflyZoneMarkers.length != 0) {
					this.noflyZoneMarkers[0].setMap(null);
					this.noflyZoneMarkers.splice(0,1);
					for(var i=0; i<this.noflyZoneMarkers.length; i++) {
						this.noflyZoneMarkers[i].index = i;
					}
				} 
				if(this.noflyZoneCircle.length != 0) {
					this.noflyZoneCircle[index].setMap(null);
					this.noflyZoneCircle.splice(index,1);
					for(var i=0; i<this.noflyZoneCircle.length; i++) {
						this.noflyZoneCircle[i].index = i;
					}
				}
			}
		} catch(err) {
			console.log(">> [uav.removeNoFlyZone()] " + err);
		}
	};
}