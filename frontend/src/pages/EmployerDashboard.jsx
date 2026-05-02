import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import { getFullName, getRole } from "../utils/auth";

function EmployerDashboard() {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    location: "",
    salary: "",
    deadline: "",
  });

  const [editJobId, setEditJobId] = useState(null);

  const fetchMyJobs = async () => {
    try {
      setLoading(true);
      const response = await api.get("/api/jobs/my-jobs");
      setJobs(response.data);
      setError("");
    } catch (err) {
      setError(err.response?.data?.message || "Failed to load employer jobs");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMyJobs();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const resetForm = () => {
    setFormData({
      title: "",
      description: "",
      location: "",
      salary: "",
      deadline: "",
    });
    setEditJobId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      if (editJobId) {
        await api.put(`/api/jobs/${editJobId}`, formData);
        setSuccess("Job updated successfully");
      } else {
        await api.post("/api/jobs", formData);
        setSuccess("Job created successfully");
      }

      resetForm();
      fetchMyJobs();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to save job");
    }
  };

  const handleEdit = (job) => {
    setEditJobId(job.id);
    setFormData({
      title: job.title,
      description: job.description,
      location: job.location,
      salary: job.salary,
      deadline: job.deadline,
    });
    setSuccess("");
    setError("");
  };

  const handleDelete = async (jobId) => {
    const confirmed = window.confirm("Are you sure you want to delete this job?");
    if (!confirmed) return;

    try {
      await api.delete(`/api/jobs/${jobId}`);
      setSuccess("Job deleted successfully");
      fetchMyJobs();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to delete job");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Employer Dashboard</h2>
      <p>Welcome, {getFullName()}</p>
      <p>Role: {getRole()}</p>

      <hr style={{ margin: "20px 0" }} />

      <h3>{editJobId ? "Edit Job" : "Create Job"}</h3>

      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}

      <form onSubmit={handleSubmit} style={{ marginBottom: "30px" }}>
        <div>
          <label>Title</label>
          <br />
          <input type="text" name="title" value={formData.title} onChange={handleChange} required />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Description</label>
          <br />
          <textarea name="description" value={formData.description} onChange={handleChange} required />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Location</label>
          <br />
          <input type="text" name="location" value={formData.location} onChange={handleChange} required />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Salary</label>
          <br />
          <input type="number" name="salary" value={formData.salary} onChange={handleChange} required />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Deadline</label>
          <br />
          <input type="date" name="deadline" value={formData.deadline} onChange={handleChange} required />
        </div>

        <button type="submit" style={{ marginTop: "15px", marginRight: "10px" }}>
          {editJobId ? "Update Job" : "Create Job"}
        </button>

        {editJobId && (
          <button type="button" onClick={resetForm} style={{ marginTop: "15px" }}>
            Cancel Edit
          </button>
        )}
      </form>

      <hr style={{ margin: "20px 0" }} />

      <h3>My Jobs</h3>

      {loading && <p>Loading jobs...</p>}

      {!loading && jobs.length === 0 && <p>No jobs posted yet.</p>}

      {!loading &&
        jobs.length > 0 &&
        jobs.map((job) => (
          <div
            key={job.id}
            style={{
              border: "1px solid #ccc",
              padding: "12px",
              marginBottom: "12px",
              borderRadius: "8px",
            }}
          >
            <h4>{job.title}</h4>
            <p>{job.description}</p>
            <p><strong>Location:</strong> {job.location}</p>
            <p><strong>Salary:</strong> {job.salary}</p>
            <p><strong>Deadline:</strong> {job.deadline}</p>

            <button onClick={() => handleEdit(job)} style={{ marginRight: "10px" }}>
              Edit
            </button>
            <button onClick={() => handleDelete(job.id)}>
              Delete
            </button>
          </div>
        ))}
    </div>
  );
}

export default EmployerDashboard;