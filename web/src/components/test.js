import axios from "axios";


const categoryName = "POPA";
const data = {name: categoryName};
try {
    await axios.put('http://localhost:8000/api/categories', data);
}
catch (error) {
    console.error('Error fetching data:', error);
}