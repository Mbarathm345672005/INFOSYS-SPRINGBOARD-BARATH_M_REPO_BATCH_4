// import logo from './logo.svg';
// import './App.css';

// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }

// export default App;


import React, { useState } from 'react';
import './App.css';

function App() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [fullName, setFullName] = useState('');
  const [companyName, setCompanyName] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState('Admin'); 
  const [contactNumber, setContactNumber] = useState('');
  const [warehouseLocation, setWarehouseLocation] = useState('Chennai');

  const handleSubmit = async (event) => {
    event.preventDefault(); 
    if (password !== confirmPassword) {
    setMessage("Passwords do not match!");
    return; 
  }
const user = {
    fullName,
    companyName,
    email,
    password,
    role,
    contactNumber,
    warehouseLocation
  };
    try {
      // Send the user data to our Spring Boot backend
      const response = await fetch('http://localhost:8080/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
      });

      const data = await response.json();
      setMessage(data.message); 

    } catch (error) {
      console.error('Error:', error);
      setMessage('Failed to connect to the server.');
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h2>Sign In</h2>
        <form onSubmit={handleSubmit}>
  {/* Full Name */}
  <div>
    <input type="text" placeholder="Full Name" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
  </div>

  {/* Company Name */}
  <div>
    <input type="text" placeholder="Company Name" value={companyName} onChange={(e) => setCompanyName(e.target.value)} required />
  </div>

  {/* Email */}
  <div>
    <input type="email" placeholder="Official Email ID" value={email} onChange={(e) => setEmail(e.target.value)} required />
  </div>

  {/* Password */}
  <div>
    <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
  </div>

  {/* Confirm Password */}
  <div>
    <input type="password" placeholder="Confirm Password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
  </div>

  {/* Role Dropdown */}
  <div>
    <label>Role:</label>
    <select value={role} onChange={(e) => setRole(e.target.value)}>
      <option value="Admin">Admin</option>
      <option value="Store Manager">Store Manager</option>
    </select>
  </div>
  
  {/* Contact Number */}
  <div>
    <input type="tel" placeholder="Contact Number" value={contactNumber} onChange={(e) => setContactNumber(e.target.value)} required />
  </div>

  {/* Warehouse Location Dropdown */}
  <div>
    <label>Warehouse Location:</label>
    <select value={warehouseLocation} onChange={(e) => setWarehouseLocation(e.target.value)}>
      <option value="Chennai">Chennai</option>
      <option value="Mumbai">Mumbai</option>
      <option value="Delhi">Delhi</option>
    </select>
  </div>

  <button type="submit">Register</button>
</form>
        {message && <p>{message}</p>}
      </header>
    </div>
  );
}

export default App;