
// eslint-disable-next-line no-unused-vars
import React from  'react';
import { useState } from 'react';
export default function ArtistForm({addFunc}){


    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');


   function handleSubmit (event){

        let artist={
            firstName:firstName,
            lastName:lastName
        }
        console.log('An artist was submitted: ');
        console.log(artist);
        addFunc(artist);
        event.preventDefault();
        setFirstName('')
        setLastName('')
    }
    return(
    <form onSubmit={handleSubmit}>
        <label>
            First Name:
            <input type="text" value={firstName} onChange={e=>setFirstName(e.target.value)} />
        </label><br/>
        <label>
            Last Name:
            <input type="text" value={lastName} onChange={e=>setLastName(e.target.value)} />
        </label><br/>

        <input type="submit" value="Add user" />
    </form>);
}