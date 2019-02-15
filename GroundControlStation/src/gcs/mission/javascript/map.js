//맵 초기

var map = {
	googlemap: null,

	gotoMake: false,
	missionMake: false,
	roiMake: false,
	fenceMake: false,
	noFlyZoneMake: false,
	
	mapFixStatus: true,
	
	uav: null,
	
	init: function() {
		try {			
			map.googlemap = new google.maps.Map(document.getElementById('map'), {
				zoom: 3,
				center: {lat:37.313778, lng:127.109004},
				//mapTypeId : "roadmap",
				mapTypeId : "satellite",
				zoomControl: false,
				streetViewControl: false,
				rotateControl: false,
				fullscreenControl: false,
				// google Map 기본 UI를 모두 안 보이게 하는 option
				disableDefaultUI: true,
				// '지도', '위성'과 같은 MapType을 control하는 UI 제거하는 option
				mapTypeControl: false
			});
			
		
			
			//지도 상에서 마우스 휠도 확대/축소할 경우 MapViewController.java의 zoomSlider의 value 변경
			document.getElementById('map').addEventListener("wheel", function() {
				jsproxy.setZoomSliderValue(map.googlemap.getZoom());
			});				
			
			map.uav = new UAV();
			
			map.googlemap.addListener('click', function(e) {
				try {
					if(map.gotoMake == true) {
						var clickLocation = {lat:e.latLng.lat(), lng:e.latLng.lng()};
						map.uav.gotoStart(clickLocation);
					} else if(map.missionMake == true) {
						map.uav.makeMissionMark("waypoint", e.latLng.lat(), e.latLng.lng(), 0, map.uav.alt);
					} else if(map.roiMake == true) {
						map.uav.makeMissionMark("roi", e.latLng.lat(), e.latLng.lng(), map.uav.roiIndex);
						jsproxy.addROI({lat:e.latLng.lat(), lng:e.latLng.lng()});
						map.roiMake = false;
					} else if(map.fenceMake == true) {
						map.uav.makeFenceMark(e.latLng.lat(), e.latLng.lng());
					} else if(map.noFlyZoneMake == true) {
						map.uav.makeNoFlyZoneMark(e.latLng.lat(), e.latLng.lng());
					}
				} catch(err) {
					console.log(">> [map.googlemap.click_function()] " + err);
				}
			});	
			
			map.googlemap.addListener('maptypeid_changed', function() {
				if(map.googlemap.getMapTypeId() == "roadmap") {
					//map.uav.setUavColor("#ffffff", "#f15f5f");
					//map.uav.setUavColor("#00a421", "#f15f5f");
				} else if(map.googlemap.getMapTypeId() == "satellite") {
					//map.uav.setUavColor("#ffffff", "#f15f5f");
					//map.uav.setUavColor("#ffff00", "#f15f5f");
				}
			});
			
			map.animation.start();
		} catch(err) {
			console.log(">> [map.init()] " + err);
		}
	},

	animation: {
		count: 1,
		start: function() {
			setInterval(function() {
				try {
					if(map.uav.currLocation != null) {
						map.uav.drawUav();	
						map.uav.drawHeadingLine();
						map.uav.drawDestLine();
					
						if(map.animation.count >= 3 & map.mapFixStatus) { 
							map.googlemap.panTo(map.uav.currLocation);
							map.animation.count = 1;
						} else {
							map.animation.count += 1;
						}
					}
					//window.requestAnimationFrame(map.uav.animation.start);
				} catch(err) {
					console.log(">> [map.animation.start()] " + err);
				}
			}, 1000);
		}
	}
};
