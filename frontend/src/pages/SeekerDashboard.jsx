import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import { getFullName, getRole } from "../utils/auth";

function SeekerDashboard() {
  const [jobs, setJobs] = useState([]);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [applicationsLoading, setApplicationsLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [searchParams, setSearchParams] = useState({
    keyword: "",
    location: "",
  });

  const [applicationData, setApplicationData] = useState({
    jobId: "",
    coverLetter: "",
    resume: null,
  });

  const fetchJobs = async (params = {}) => {
    try {
      setLoading(true);
      const response = await api.get("/api/jobs/search", { params });
      setJobs(response.data);
      setError("");
    } catch (err) {
      setError(err.response?.data?.message || "Failed to load jobs");
    } finally {
      setLoading(false);
    }
  };

  const fetchMyApplications = async () => {
    try {
      setApplicationsLoading(true);
      const response = await api.get("/api/applications/my-applications");
      setApplications(response.data);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to load applications");
    } finally {
      setApplicationsLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs();
    fetchMyApplications();
  }, []);

  const handleSearchChange = (e) => {
    setSearchParams({
      ...searchParams,
      [e.target.name]: e.target.value,
    });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setSuccess("");
    await fetchJobs(searchParams);
  };

  const handleApplicationChange = (e) => {
    const { name, value, files } = e.target;
    setApplicationData({
      ...applicationData,
      [name]: files ? files[0] : value,
    });
  };

  const handleApply = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      const formData = new FormData();
      formData.append("jobId", applicationData.jobId);
      formData.append("coverLetter", applicationData.coverLetter);
      formData.append("resume", applicationData.resume);

      await api.post("/api/applications/apply", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

  await fetchMyApplications();

      setSuccess("Application submitted successfully");
      setApplicationData({
        jobId: "",
        coverLetter: "",
        resume: null,
      });
    } catch (err) {
      setError(err.response?.data?.message || "Failed to apply for job");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Seeker Dashboard</h2>
      <p>Welcome, {getFullName()}</p>
      <p>Role: {getRole()}</p>

      <hr style={{ margin: "20px 0" }} />

      <h3>Search Jobs</h3>

      <form onSubmit={handleSearch} style={{ marginBottom: "20px" }}>
        <div>
          <label>Keyword</label>
          <br />
          <input type="text" name="keyword" value={searchParams.keyword} onChange={handleSearchChange} />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Location</label>
          <br />
          <input type="text" name="location" value={searchParams.location} onChange={handleSearchChange} />
        </div>

        <button type="submit" style={{ marginTop: "15px" }}>
          Search
        </button>
      </form>

      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}

      <h3>Available Jobs</h3>

      {loading && <p>Loading jobs...</p>}

      {!loading && jobs.length === 0 && <p>No jobs available.</p>}

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
            <p><strong>Job ID:</strong> {job.id}</p>
          </div>
        ))}

      <hr style={{ margin: "20px 0" }} />

      <h3>Apply to Job</h3>

      <form onSubmit={handleApply}>
        <div>
          <label>Job ID</label>
          <br />
          <input
            type="number"
            name="jobId"
            value={applicationData.jobId}
            onChange={handleApplicationChange}
            required
          />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Cover Letter</label>
          <br />
          <textarea
            name="coverLetter"
            value={applicationData.coverLetter}
            onChange={handleApplicationChange}
            required
          />
        </div>

        <div style={{ marginTop: "10px" }}>
          <label>Resume</label>
          <br />
          <input
            type="file"
            name="resume"
            accept=".pdf,.doc,.docx"
            onChange={handleApplicationChange}
            required
          />
        </div>

        <button type="submit" style={{ marginTop: "15px" }}>
          Apply
        </button>
      </form>

      <hr style={{ margin: "20px 0" }} />

      <h3>My Applications</h3>

      {applicationsLoading && <p>Loading applications...</p>}

      {!applicationsLoading && applications.length === 0 && (
        <p>No applications submitted yet.</p>
      )}

      {!applicationsLoading &&
        applications.length > 0 &&
        applications.map((application) => (
          <div
            key={application.id}
            style={{
              border: "1px solid #ccc",
              padding: "12px",
              marginBottom: "12px",
              borderRadius: "8px",
            }}
          >
            <p><strong>Application ID:</strong> {application.id}</p>
            <p><strong>Job Title:</strong> {application.jobTitle}</p>
            <p><strong>Status:</strong> {application.status}</p>
            <p><strong>Applied At:</strong> {application.appliedAt}</p>
            <p><strong>Resume:</strong> {application.resumeUrl}</p>
          </div>
        ))}
    </div>
  );
}

export default SeekerDashboard;