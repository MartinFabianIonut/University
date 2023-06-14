import { useState } from 'react'
import React, {useEffect} from 'react';
import './ArtistsApp.css'
import ArtistTable from './ArtistTable.jsx';
import ArtistForm from "./ArtistForm.jsx";
import {GetArtists, AddArtist, UpdateArtist, DeleteArtist} from "./utils/REST-calls.js";

export default function ArtistsApp(){
    const [artists, setArtists] = useState([{"firstName":"maria","lastName":"abrudan","id":123}]);

    function addFunc(artist){
        console.log('inside add Func '+artist);
        AddArtist(artist)
            .then(()=> GetArtists())
            .then(artists=>setArtists(artists))
            .catch(erorr=>console.log('eroare add ',erorr));
    }

    function updateFunc(artist){
        console.log('inside update Func '+artist);
        UpdateArtist(artist)
            .then(()=> GetArtists())
            .then(artists=>setArtists(artists))
            .catch(erorr=>console.log('eroare update ',erorr));
    }

    function deleteFunc(artist){
        console.log('inside deleteFunc '+artist);
        DeleteArtist(artist)
            .then(()=> GetArtists())
            .then(artists=>setArtists(artists))
            .catch(error=>console.log('eroare delete', error));
    }
    useEffect(()=>{
        console.log('inside useEffect')
        GetArtists().then(artists=>setArtists(artists));},[]);

    return (<div className="ArtistApp">
        <h2> New Artist Management App </h2>
        <div><p>Add an artist: </p></div>
        <ArtistForm addFunc={addFunc}/>
        <br/>
        <br/>
        <ArtistTable artists={artists} deleteFunc={deleteFunc} updateFunc={updateFunc}/>
    </div>);
}
