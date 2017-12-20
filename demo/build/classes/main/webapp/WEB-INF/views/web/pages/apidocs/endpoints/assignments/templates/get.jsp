<h2>Get Assignment Template</h2>
<p><em>https://www.workmarket.com/api/v1/<strong>assignments/templates/get</strong></em></p>

<p>Get assignment details</p>

<table>
	<tr>
		<td>HTTP Method</td>
		<td><code>GET</code></td>
	</tr>
	<tr>
		<td>Requires Authentication</td>
		<td>Yes</td>
	</tr>
</table>

<h3>Parameters</h3>
<p>All parameters are optional, unless otherwise indicated.</p>

<table>
	<tbody>
		<tr>
			<td><code>id</code></td>
			<td>6029116324</td>
			<td><span class="required"></span>Template ID</td>
		</tr>
	</tbody>
</table>

<h3>Response fields</h3>
<table>
	<tr>
		<td><code>template_id</code></td>
		<td>Template ID</td>
	</tr>
	<tr>
		<td><code>title</code></td>
		<td>Assignment title</td>
	</tr>
	<tr>
		<td><code>description</code></td>
		<td>Assignment description</td>
	</tr>
	<tr>
		<td><code>instructions</code></td>
		<td>Assignment instructions</td>
	</tr>
	<tr>
		<td><code>desired_skills</code></td>
		<td>Desired skills for a worker</td>
	</tr>
	<tr>
		<td><code>project</code></td>
		<td>Associated project</td>
	</tr>
	<tr>
		<td><code>client</code></td>
		<td>Company the assignment is for</td>
	</tr>
	<tr>
		<td><code>internal_owner</code></td>
		<td>Owner of the assignment</td>
	</tr>
	<tr>
		<td><code>scheduled_start</code></td>
		<td>Assignment start date and time</td>
	</tr>
	<tr>
		<td><code>scheduled_end</code></td>
		<td>End of scheduled window</td>
	</tr>
	<tr>
		<td><code>industry</code></td>
		<td>Assignment industry</td>
	</tr>
	<tr>
		<td><code>time_zone</code></td>
		<td>Time zone in which the assignment is occurring</td>
	</tr>
	<tr>
		<td><code>required_attachments</code></td>
		<td>Number of required attachments</td>
	</tr>
	<tr>
		<td><code>location_offsite</code></td>
		<td>A boolean specifying if the assignment can be completed off-site. If this is set to <code>FALSE</code>, then the <code>location</code> field must be set</td>
	</tr>
	<tr>
		<td><code>location</code></td>
		<td>An object containing an <code>id</code></td>
	</tr>
	<tr>
		<td><code>location_contact</code></td>
		<td>An array containing user objects with a <code>first_name</code>, <code>last_name</code>, <code>email</code>, and an array of <code>phone_numbers</code> with <code>phone</code>, <code>extension</code> and <code>type</code></td>
	</tr>
	<tr>
		<td><code>support_contact</code></td>
		<td>An array containing user objects with a <code>first_name</code>, <code>last_name</code>, <code>email</code>, and an array of <code>phone_numbers</code> with <code>phone</code>, <code>extension</code> and <code>type</code></td>
	</tr>
	<tr>
		<td><code>pricing</code></td>
		<td>An object containing <code>type</code>, <code>spend_limit</code> and <code>additional_expenses</code>. Additionally contains some of the following, conditional on the <code>type</code> of pricing: <code>flat_price</code>, <code>per_hour_price</code>, <code>max_number_of_hours</code>, <code>per_unit_price</code>, <code>max_number_of_units</code>, <code>initial_per_hour_price</code>, <code>initial_number_of_hours</code>, <code>additional_per_hour_price</code> and <code>max_blended_number_of_hours</code></td>
	</tr>
	<tr>
		<td><code>attachments</code></td>
		<td>An array of attachment objects containing <code>name</code>, <code>description</code> and <code>relative_uri</code></td>
	</tr>
	<tr>
		<td><code>custom_fields</code></td>
		<td>An object containing <code>id</code>, <code>name</code> and an array of <code>fields</code>. <code>fields</code> contains custom field objects with an <code>id</code>, <code>name</code>, <code>value</code>, <code>default</code> and <code>required</code></td>
	</tr>
</table>