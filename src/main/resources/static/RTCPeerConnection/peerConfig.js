

//let video = document.querySelector('#remoteStream');
let localStreamElement = document.querySelector('#localStream');
//const myKey = Math.random().toString(36).substring(2, 11);
const myKey = document.querySelector("#otherKey").value;
const userEmail = document.querySelector("#userEmail").value;
const doctorEmail = document.querySelector("#doctorEmail").value;

let pcListMap = new Map();
let roomId;
let userIdList = [];
let localStream = undefined;

let video2 = undefined;

const userId = "test";
const peerConnection = null;

//연결끊기 위해서 밖으로 빼봄
const socket = new SockJS('/signaling');   //endpoint
stompClient = Stomp.over(socket);
stompClient.debug = null;

let pc = new RTCPeerConnection();


//음소거, 화면끄기
const muteBtn = document.getElementById("mute");
const cameraBtn = document.getElementById("camera");
const videoCam = document.getElementById("videoCam");
const mic = document.getElementById("mic");

let muted = false;
let cameraOff = false;

//캠 화면켜기
const startCam = async () =>{

   console.log("start Cam");
   
    if(navigator.mediaDevices !== undefined)
    {
       // .then : (fucntion(mediaStream){ }) 처럼 받은 스트림 객체를 함수에 전달하고 그 함수 안에서 스트림을 통해서 필요한 기능을 구현합니다.
       
        await navigator.mediaDevices.getUserMedia({ audio: true, video : true })
            .then(async (stream) => {   
            
                console.log('Stream found');
                localStream = stream;
                console.log(localStreamElement);
                // Disable the microphone by default
                stream.getAudioTracks()[0].enabled = true;
                localStreamElement.srcObject = localStream;
                // Connect after making sure that local stream is availble

            }).catch(error => {
                console.error("Error accessing media devices:", error);
            });
    }
    
      console.log(stompClient);
}


const connectSocket = async () =>{
   
    stompClient.connect({}, function () {
        console.log('Connected to WebRTC server');
         
         startStream();
         
         //subscribe: 특정 stomp 브로커의 location을 구독한다.
        stompClient.subscribe(`/topic/peer/iceCandidate/${myKey}/${roomId}`, candidate => {
           console.log("subscribe 1111111111111111");
            const key = JSON.parse(candidate.body).key
            const message = JSON.parse(candidate.body).body;
         console.log(key);
         console.log(message);
   
            pcListMap.get(key).addIceCandidate(new RTCIceCandidate({candidate:message.candidate,sdpMLineIndex:message.sdpMLineIndex,sdpMid:message.sdpMid}));
         
         
         console.log(stompClient);   
        });
      
      
      
        stompClient.subscribe(`/topic/peer/offer/${myKey}/${roomId}`, offer => {
           console.log("subscribe 222222222222222222");
           console.log(offer);
            const key = JSON.parse(offer.body).key;
            const message = JSON.parse(offer.body).body;

            pcListMap.set(key,createPeerConnection(key));
            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription({type:message.type,sdp:message.sdp}));
            sendAnswer(pcListMap.get(key), key);
        });

        stompClient.subscribe(`/topic/peer/answer/${myKey}/${roomId}`, answer =>{
           console.log("subscribe 33333333333333333333");
           console.log(answer);
            const key = JSON.parse(answer.body).key;
            const message = JSON.parse(answer.body).body;

            pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(message));

        });

        stompClient.subscribe(`/topic/call/key`, message =>{
           console.log("sendKey 1111111111");
            stompClient.send(`/app/send/key`, {}, JSON.stringify(myKey));
   
        });

        stompClient.subscribe(`/topic/send/key`, message => {
           console.log("sendKey 222222222222");
            const key = JSON.parse(message.body);

            if(myKey !== key && userIdList.find((mapKey) => mapKey === myKey) === undefined){
                userIdList.push(key);
            }
            
        });


    });
    
}


let onTrack = (event, userId) => {
   
   console.log("onTrack입니다");
   console.log("id값 찍어보기 : " + document.getElementById(`${userId}`));
    if(document.getElementById(`${userId}`) === null){
       const video = document.querySelector('#remoteStream');
        console.log("userId : " + `${userId}`);
        video.autoplay = true;
        video.id = userId;
        video.srcObject = event.streams[0];
   
      video2 = video;
    }
   
    //remoteStreamElement.srcObject = event.streams[0];
    // remoteStreamElement.play();
};



const createPeerConnection = (userId) =>{
    //const pc = new RTCPeerConnection();
    
    
    console.log("pearConnection입니다");
    console.log(userId);
    console.log(pc.size);
    console.log(pcListMap.size);
    
       try {
           pc.addEventListener('icecandidate', (event) =>{
               onIceCandidate(event, userId);
           });
           pc.addEventListener('track', (event) =>{
               onTrack(event, userId);
           });
           if(localStream !== undefined){
               localStream.getTracks().forEach(track => {
                   pc.addTrack(track, localStream);
               });
           }

           console.log('PeerConnection created');
       } catch (error) {
           console.error('PeerConnection failed: ', error);
       }
       
     document.querySelector('#endSteamBtn').style.display = '';
    
    return pc;
}




//offer 넘겨주기
let sendOffer = (pc ,userId) => {
    pc.createOffer().then(offer =>{
        setLocalAndSendMessage(pc, offer);
        stompClient.send(`/app/peer/offer/${userId}/${roomId}`, {}, JSON.stringify({
            key : myKey,
            body : offer
        }));
        console.log('Send offer');
    });
};


//answer 받고 넘겨주기
let sendAnswer = (pc,userId) => {
    pc.createAnswer().then( answer => {
        setLocalAndSendMessage(pc ,answer);
        stompClient.send(`/app/peer/answer/${userId}/${roomId}`, {}, JSON.stringify({
            key : myKey,
            body : answer
        }));
        console.log('Send answer');
    });
};


let onIceCandidate = (event, userId) => {
    if (event.candidate) {
        console.log('ICE candidate');
        stompClient.send(`/app/peer/iceCandidate/${userId}/${roomId}`,{}, JSON.stringify({
            key : myKey,
            body : event.candidate
        }));
    }
};



const setLocalAndSendMessage = (pc ,sessionDescription) =>{
    pc.setLocalDescription(sessionDescription);
    
    console.log("=====================");
    console.log(sessionDescription)
    
}



//룸 번호 입력 후 캠 + 웹소켓 실행
/*
document.querySelector('#enterRoomBtn').addEventListener('click', async () =>{
    await startCam();

   
    roomId = document.querySelector('#roomIdInput').value;
    document.querySelector('#roomIdInput').disabled = true;
    document.querySelector('#enterRoomBtn').disabled = true;
   
   

    await connectSocket();
});
*/

    startCam();

    if(localStream !== undefined){
        document.querySelector('#localStream').style.display = 'block';
        document.querySelector('#startSteamBtn').style.display = '';
        //document.querySelector('#mute').style.display = '';
      //document.querySelector('#camera').style.display = '';
    }
    
    roomId = document.querySelector('#roomIdInput').value;
    console.log("roomId : " + roomId);
    document.querySelector('#roomIdInput').disabled = true;
   
   

    connectSocket();


// 스트림 버튼 클릭시 , 다른 웹 key들 웹소켓을 가져 온뒤에 offer -> answer -> iceCandidate 통신
// peer 커넥션은 pcListMap 으로 저장


async function startStream(){
   console.log("startsteamBtn click 실행");
    await stompClient.send(`/app/call/key`, {}, {});
   console.log("callKey 111111111111111");
    setTimeout(() =>{
       userIdList.map((key) =>{
            if(!pcListMap.has(key)){
                pcListMap.set(key, createPeerConnection(key));
                sendOffer(pcListMap.get(key),key);
            }

        });

    },2000);
}

/*
//map.has 메서드 안에 들어있는 인수가 map 안에 들어있으면 true 반환 없으면 false 반환
        userIdList.map((key) =>{
            if(!pcListMap.has(key)){
                pcListMap.set(key, createPeerConnection(key));
                sendOffer(pcListMap.get(key),key);
            }

        });

    },1000);
});
*/


//강제 연결 끊기
document.querySelector('#endSteamBtn').addEventListener('click', () =>{
   console.log("연결 종료할거임");
   
   pc.close();
   
   pc.addEventListener('iceconnectionstatechange', function(e) { 
         console.log('ice 상태 변경', pc.iceConnectionState); 
   });
   
   if(userEmail == myKey)
   {
      alert("연결이 종료되었습니다.");
      
      //TODO : 민지 수정부분(결제페이지)
      reservationSeq = $("#reservationSeq").val();
      location.href = "/clinic-reserve-payment-page?reservationSeq=" + reservationSeq;
   }
   
   if(doctorEmail == myKey)
   {


/////////////////////////////// 진료 종료 누르면 가는 ajax /////////////////////////////////
       $.ajax({
               type:"POST",
               url:"/streamEnd",
               data:{
                  reservationSeq : document.querySelector("#reservationSeq").value
               },
               success:function(response)
               {
                  if(response == 0)
                        {
                     alert("연결이 종료되었습니다.");
                           location.href = "/reservationList-page";
                        }
                        else if(response == 1)
                        {
                           alert("오류");
                        }
                        else
                        {
                           alert("오류 발생");
                           location.href = "/mypage-page;"
                        }
                           
               },
               error: function(xhr, err) 
               {
                   alert("진료완료 실패");
                 }
          });
   }
});




//음소거
function handleMuteClick(){
    localStream.getAudioTracks().forEach((track) => (track.enabled = !track.enabled));
    //위와같은 화살표함수
    //track.enabled라는 속성은 화면을 끄는 속성을 클릭할 때마다 서로 반대로 입력해주는 과정
    
    //localStream.getAudioTracks().forEach(function(track){
    //    track.enabled = !track.enabled;
    //})
    
    
    if(!muted){
        mic.innerText = "mic_off";
        muted = true;
    } else{
        mic.innerText = "mic";
        muted = false;
    }
    
}

//화면끄기
function handleCameraClick(){
    localStream.getVideoTracks().forEach((track) => (track.enabled = !track.enabled));
    if(cameraOff){
        videoCam.innerText = "videocam";
        cameraOff = false;
    } else {
        videoCam.innerText = "videocam_off";
        cameraOff = true;
    }
   
}


muteBtn.addEventListener("click", handleMuteClick);
cameraBtn.addEventListener("click", handleCameraClick);


//연결상태 변경을 알리는 이벤트
pc.addEventListener('iceconnectionstatechange', function(e) { 
   console.log('ice 상태 변경', pc.iceConnectionState);
   
   handleChangeIcecandidate(pc.iceConnectionState)
});


function handleChangeIcecandidate(change){
   if(change == "disconnected")   
   {
      if(userEmail == myKey)
       {
          alert("연결이 종료되었습니다.");
           //TODO : 민지 수정부분(결제페이지)
	      	reservationSeq = $("#reservationSeq").val();
            location.href = "/clinic-reserve-payment-page?reservationSeq=" + reservationSeq;

       }
   
       if(doctorEmail == myKey)
       {


/////////////////////////////// 상대방이 종료 누르면 가는 ajax /////////////////////////////////
              
           $.ajax({
               type:"POST",
               url:"/streamEnd",
               data:{
                  reservationSeq : document.querySelector("#reservationSeq").value
               },
               success:function(response)
               {
                  if(response == 0)
                        {
                     alert("연결이 종료되었습니다.");
                           location.href = "/reservationList-page";
                        }
                        else if(response == 1)
                        {
                           alert("오류");
                        }
                        else
                        {
                           alert("오류 발생");
                           location.href = "/mypage-page;"
                        }
                           
               },
               error: function(xhr, err) 
               {
                   alert("진료완료 실패");
                 }
          });
       }
   }
};