import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';

function App() {
    const [responseData, setResponseData] = useState({});

    useEffect(() => {
        handleAxiosRequest();
    }, []);

    const handleAxiosRequest = () => {
        axios
            .get('http://localhost:8000/api/good/1')
            .then((response) => {
                console.log(response.data);
                setResponseData(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    return (
        <div className="App">
            <h1>React App</h1>
            <div>
                <h2>Response Data:</h2>
                <pre>{JSON.stringify(responseData, null, 2)}</pre>
            </div>
        </div>
    );
}

ReactDOM.render(<App />, document.getElementById('root'));
