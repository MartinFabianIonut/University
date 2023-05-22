
// eslint-disable-next-line no-unused-vars
import React, {useState} from 'react';
import './ArtistsApp.css'

// eslint-disable-next-line react/prop-types
function ArtistRow({artist, deleteFunc, updateFunc}){


    const [updatedFirstName, setUpdatedFirstName] = useState(artist.firstName);
    const [updatedLastName, setUpdatedLastName] = useState(artist.lastName);
    const [isEditing, setIsEditing] = useState(false);

    function handleDelete(){
        console.log('delete button pentru '+artist.id);
        deleteFunc(artist.id);
    }
    function handleUpdate(){
        artist.firstName=updatedFirstName;
        artist.lastName=updatedLastName;
        console.log('update button pentru '+artist.id);
        updateFunc(artist);
    }
    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleCancel = () => {
        setIsEditing(false);
        setUpdatedFirstName(artist.firstName);
        setUpdatedLastName(artist.lastName);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        handleUpdate();
        setIsEditing(false);
    };

    return (
        <tr>
            <td>
                {isEditing ? (
                    <input
                        type="text"
                        value={updatedFirstName}
                        onChange={(e) => setUpdatedFirstName(e.target.value)}
                    />
                ) : (
                    artist.firstName
                )}
            </td>
            <td>
                {isEditing ? (
                    <input
                        type="text"
                        value={updatedLastName}
                        onChange={(e) => setUpdatedLastName(e.target.value)}
                    />
                ) : (
                    artist.lastName
                )}
            </td>
            <td>
                {isEditing ? (
                    <>
                        <button onClick={handleSubmit}>Save</button>
                        <button onClick={handleCancel}>Cancel</button>
                    </>
                ) : (
                    <>
                        <button onClick={handleDelete}>Delete</button>
                        <button onClick={handleEdit}>Update</button>
                    </>
                )}
            </td>
        </tr>
    );
}

export default function ArtistTable({artists, deleteFunc, updateFunc}){
    console.log("In ArtistTable");
    console.log(artists);
    let rows = [];
    artists.forEach(function(artist) {
        rows.push(<ArtistRow artist={artist} key={artist.id} deleteFunc={deleteFunc} updateFunc={updateFunc} />);
    });
    return (
        <div className="ArtistTable">

            <table className="center">
                <thead>
                <tr>
                    <th>First name</th>
                    <th>Last name</th>

                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

        </div>
    );
}